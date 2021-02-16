package com.web.curation.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.notification.NotiListDto;
import com.web.curation.dto.notification.NotiSettingsDto;
import com.web.curation.dto.notification.UserNotiDto;
import com.web.curation.exceptions.TypeNotDefineException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.notification.NotificationRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public void subscribeAll(NotiSettingsDto notiSettingsDto) throws FirebaseMessagingException {
        List<String> tokenList = new ArrayList<>();
        tokenList.add(notiSettingsDto.getRegiToken());

        TopicManagementResponse response1 = FirebaseMessaging.getInstance().subscribeToTopic(tokenList, "memory-" + notiSettingsDto.getUserId());
        System.out.println(response1.getSuccessCount() +"  "+response1.getFailureCount()+"  "+response1.getErrors().size());
        response1.getErrors().stream().forEach(error -> System.out.println(error.toString()));

        TopicManagementResponse response2 = FirebaseMessaging.getInstance().subscribeToTopic(tokenList, "follow-" + notiSettingsDto.getUserId());
        System.out.println(response2.getSuccessCount() +"  "+response2.getFailureCount()+"  "+response2.getErrors().size());
        response2.getErrors().stream().forEach(error -> System.out.println(error.toString()));

        TopicManagementResponse response3 = FirebaseMessaging.getInstance().subscribeToTopic(tokenList, "like-" + notiSettingsDto.getUserId());
        System.out.println(response3.getSuccessCount() +"  "+response3.getFailureCount()+"  "+response3.getErrors().size());
        response3.getErrors().stream().forEach(error -> System.out.println(error.toString()));

        User user = getUser(notiSettingsDto.getUserId());
        user.setMemoryNoti(true);
        user.setFollowNoti(true);
        user.setLikeNoti(true);
    }

    public void subscribe(NotiSettingsDto notiSettingsDto) throws FirebaseMessagingException {
        List<String> tokenList = new ArrayList<>();
        tokenList.add(notiSettingsDto.getRegiToken());

        User user = getUser(notiSettingsDto.getUserId());
        switch (notiSettingsDto.getType()) {
            case "memory":
                user.setMemoryNoti(true);
                break;
            case "follow":
                user.setFollowNoti(true);
                break;
            case "like":
                user.setLikeNoti(true);
                break;
            default:
                throw new TypeNotDefineException(notiSettingsDto.getType());
        }
        System.out.println("이벤트 구독: " + notiSettingsDto.getType() + "-" + notiSettingsDto.getUserId());
        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(tokenList, notiSettingsDto.getType() + "-" + notiSettingsDto.getUserId());
        System.out.println(response.getSuccessCount() +"  "+response.getFailureCount()+"  "+response.getErrors().size());
        response.getErrors().stream().forEach(error -> System.out.println(error.toString()));
    }

    public void unsubscribe(NotiSettingsDto notiSettingsDto) throws FirebaseMessagingException {
        List<String> tokenList = new ArrayList<>();
        tokenList.add(notiSettingsDto.getRegiToken());

        User user = getUser(notiSettingsDto.getUserId());
        switch (notiSettingsDto.getType()) {
            case "memory":
                user.setMemoryNoti(false);
                break;
            case "follow":
                user.setFollowNoti(false);
                break;
            case "like":
                user.setLikeNoti(false);
                break;
            default:
                throw new TypeNotDefineException(notiSettingsDto.getType());
        }

        System.out.println("이벤트 구독 해제:" + notiSettingsDto.getType() + "-" + notiSettingsDto.getUserId());
        System.out.println(notiSettingsDto.getRegiToken());
        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(tokenList, notiSettingsDto.getType() + "-" + notiSettingsDto.getUserId());
        System.out.println(response.getSuccessCount() +"  "+response.getFailureCount()+"  "+response.getErrors().size());
        response.getErrors().stream().forEach(error -> System.out.println(error.toString()));
    }

    public UserNotiDto getNotiInfo(String userId) {
        User user = getUser(userId);
        return new UserNotiDto(user);
    }

    public long getCountUncheckNoti(String userId) {
        User user = getUser(userId);
        return notificationRepository.countByToAndChecked(user, false);
    }

    public List<NotiListDto> getUncheckNoti(String userId) {
        User user = getUser(userId);
        List<NotiListDto> result = notificationRepository.findByToAndChecked(user, false).stream()
                .map(noti -> {
//                    noti.setChecked(true);
                    return new NotiListDto(noti);
                })
                .collect(Collectors.toList());
        return result;
    }

    public List<NotiListDto> getcheckNoti(String userId) {
        User user = getUser(userId);
        List<NotiListDto> result = notificationRepository.findByToAndChecked(user, true).stream()
                .map(noti -> {
                    return new NotiListDto(noti);
                })
                .collect(Collectors.toList());
        return result;
    }

    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
        return user;
    }

}
