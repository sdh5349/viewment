package com.web.curation.service.user;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import com.web.curation.dto.notification.FirebaseNotiDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * com.web.curation.service.user
 * FollowServiceImpl.java
 *
 * @author 김종성
 * @date 2021-01-25 오전 11:19
 * @변경이력
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
        return user;
    }

    @Override
    public Long follow(String userId, String targetUserId) throws FirebaseMessagingException {
        User from = getUser(userId);
        User to = getUser(targetUserId);

        followRepository.findByFromAndTo(from, to).ifPresent(
                m -> {
                    throw new IllegalStateException("이미 팔로우하고 있는 회원입니다.");
                }
        );

        Follow follow = new Follow();
        follow.setFrom(from);
        follow.setTo(to);

        followRepository.save(follow);

        if (to.isFollowNoti()) {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle("Viewment")
                            .setBody(from.getNickname() + " 님이 팔로우 하셨습니다")
                            .build())
                    .setTopic("follow-" + to.getId())
                    .build();
            FirebaseMessaging.getInstance().send(message);
            saveNoti(to, from);
        }
        return follow.getId();
    }

    private void saveNoti(User to, User from) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("noti/" + to.getId());
        ref.push().setValueAsync(new FirebaseNotiDto(to, from));
    }

    @Override
    public void unfollow(String userId, String targetUserId) {
        Follow follow = followRepository.findByUserIdAndTargetUserId(userId, targetUserId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Follow", "follow_Id");
                }
        );
        followRepository.delete(follow);
    }

    @Override
    public Page<SimpleUserInfoDto> findFollowingList(String currentUserId, String userId, Pageable pageable) {
        User currentUser = getUser(currentUserId);
        User user = getUser(userId);

        List<User> currentUserFollowings = followRepository.findByFrom(currentUser).stream()
                .map(follow -> {
                    return follow.getTo();
                })
                .collect(Collectors.toList());

        Page<SimpleUserInfoDto> result = followRepository.findByFrom(user, pageable).map(
                follow -> {
                    return new SimpleUserInfoDto(follow.getTo(), currentUserFollowings.contains(follow.getTo()));
                }
        );

        return result;
    }

    @Override
    public Page<SimpleUserInfoDto> findFollowerList(String currentUserId, String userId, Pageable pageable) {
        User currentUser = getUser(currentUserId);
        User user = getUser(userId);

        List<User> currentUserFollowings = followRepository.findByFrom(currentUser).stream()
                .map(follow -> {
                    return follow.getTo();
                })
                .collect(Collectors.toList());

        Page<SimpleUserInfoDto> result = followRepository.findByTo(user, pageable).map(
                follow -> {
                    return new SimpleUserInfoDto(follow.getFrom(), currentUserFollowings.contains(follow.getFrom()));
                }
        );

        return result;
    }

    @Override
    public int getSizeofFollowers(String userId) {
        User user = getUser(userId);
        return followRepository.findByTo(user).size();
    }

    @Override
    public int getSizeofFollowings(String userId) {
        User user = getUser(userId);
        return followRepository.findByFrom(user).size();
    }
}
