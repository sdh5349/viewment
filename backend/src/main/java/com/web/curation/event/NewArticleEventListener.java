package com.web.curation.event;

import com.google.firebase.messaging.*;
import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import com.web.curation.repository.memory.MemoryPinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Async
@Transactional
@Component
@RequiredArgsConstructor
public class NewArticleEventListener {

    private final MemoryPinRepository memoryPinRepository;

    @EventListener
    public void handleNewArticleEvent(NewArticleEvent newArticleEvent) throws FirebaseMessagingException {
        System.out.println("이벤트 핸들");
        List<Message> messages = new ArrayList<>();
        Pin pin = newArticleEvent.getArticle().getPin();
        memoryPinRepository.findByPin(pin).stream()
                .forEach(memoryPin -> {
                    Memory memory = memoryPin.getMemory();
                    Message message = Message.builder()
                            .setNotification(Notification.builder()
                                    .setTitle("Viewment")
                                    .setBody(memory.getName()+"에 새로운 사진이 등록되었습니다")
                                    .build())
                            .setTopic("memory-"+memory.getId())
                            .build();
                    messages.add(message);
                });
        FirebaseMessaging.getInstance().sendAll(messages);
    }
}
