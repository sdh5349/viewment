package com.web.curation.event;

import com.google.firebase.messaging.*;
import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.notification.Noti;
import com.web.curation.domain.notification.NotiType;
import com.web.curation.repository.memory.MemoryPinRepository;
import com.web.curation.repository.notification.NotificationRepository;
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
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleNewArticleEvent(NewArticleEvent newArticleEvent) throws FirebaseMessagingException {
        System.out.println("이벤트 핸들");
        List<Message> messages = new ArrayList<>();
        Pin pin = newArticleEvent.getArticle().getPin();
        memoryPinRepository.findByPin(pin).stream()
                .forEach(memoryPin -> {
                    Memory memory = memoryPin.getMemory();
                    if (memory.getUser().isMemoryNoti()) {
                        Message message = Message.builder()
                                .setNotification(Notification.builder()
                                        .setTitle("Viewment")
                                        .setBody(memory.getName() + "에 새로운 사진이 등록되었습니다")
                                        .build())
                                .setTopic("memory-" + memory.getUser().getId())
                                .build();
                        messages.add(message);
                        System.out.println("이벤트 발생: " + "memory-" + memory.getUser().getId());
                        saveNotification(memory, newArticleEvent.getArticle());
                    }
                });
        if (messages.size() > 0)
            FirebaseMessaging.getInstance().sendAll(messages);
    }

    private void saveNotification(Memory memory, Article article) {
        Noti noti = new Noti();
        noti.setTo(memory.getUser());
        noti.setMemory(memory);
        noti.setArticle(article);
        noti.setChecked(false);
        noti.setNotiType(NotiType.MEMORY);
        notificationRepository.save(noti);
    }
}
