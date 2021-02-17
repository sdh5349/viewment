package com.web.curation.event;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.web.curation.domain.User;
import com.web.curation.dto.notification.FirebaseNotiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Async
@Transactional
@Component
@RequiredArgsConstructor
public class NewFollowerEventListener {

    @EventListener
    public void handleNewFollowerEvent(NewFollowerEvent newFollowerEvent) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("Viewment")
                        .setBody(newFollowerEvent.getFrom().getNickname() + " 님이 팔로우 하셨습니다")
                        .build())
                .setTopic("follow-" + newFollowerEvent.getTo().getId())
                .build();
//        System.out.println("이벤트 발생: " + "follow-" + newFollowerEvent.getTo().getId());
        FirebaseMessaging.getInstance().send(message);
        saveNoti(newFollowerEvent.getTo(), newFollowerEvent.getFrom());
    }

    private void saveNoti(User to, User from) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("noti/"+to.getId());
        ref.push().setValueAsync(new FirebaseNotiDto(to, from));
    }

}
