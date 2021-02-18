package com.web.curation.event;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
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
public class NewLikeEventListener {

    @EventListener
    public void handleNewLikeEvent(NewLikeEvent newLikeEvent) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("Viewment")
                        .setBody(newLikeEvent.getFrom().getNickname() + " 님이 회원님의 글을 좋아합니다")
                        .build())
                .setTopic("like-" + newLikeEvent.getTo().getId())
                .build();
//        System.out.println("이벤트 발생: " + "like-" + newLikeEvent.getTo().getId());
        FirebaseMessaging.getInstance().send(message);
        saveNoti(newLikeEvent.getTo(), newLikeEvent.getFrom(), newLikeEvent.getArticle());
    }

    private void saveNoti(User to, User from, Article article) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("noti/"+to.getId());
        ref.push().setValueAsync(new FirebaseNotiDto(to, from, article));
    }
}
