package com.web.curation.event;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.web.curation.domain.notification.Noti;
import com.web.curation.domain.notification.NotiType;
import com.web.curation.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Async
@Transactional
@Component
@RequiredArgsConstructor
public class NewLikeEventListener {

    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleNewLikeEvent(NewLikeEvent newLikeEvent) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("Viewment")
                        .setBody(newLikeEvent.getFrom().getNickname() + " 님이 회원님의 글을 좋아합니다")
                        .build())
                .setTopic("like-" + newLikeEvent.getTo().getId())
                .build();
        System.out.println("이벤트 발생: " + "like-" + newLikeEvent.getTo().getId());
        FirebaseMessaging.getInstance().send(message);
        saveNotification(newLikeEvent);
    }

    private void saveNotification(NewLikeEvent newLikeEvent) {
        Noti noti = new Noti();
        noti.setTo(newLikeEvent.getTo());
        noti.setFrom(newLikeEvent.getFrom());
        noti.setArticle(newLikeEvent.getArticle());
        noti.setChecked(false);
        noti.setNotiType(NotiType.LIKE);
        notificationRepository.save(noti);
    }
}
