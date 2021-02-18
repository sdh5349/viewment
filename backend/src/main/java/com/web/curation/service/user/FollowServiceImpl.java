package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.event.NewArticleEvent;
import com.web.curation.event.NewFollowerEvent;
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
 * @date    2021-01-25 오전 11:19
 * @author  김종성
 *
 * @변경이력
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public User getUser(String userId){
        User user = userRepository.findById(userId).orElseThrow(
                ()->{throw new UserNotFoundException(); }
        );
        return user;
    }

    @Override
    public Long follow(String userId, String targetUserId) {
        User from = getUser(userId);
        User to = getUser(targetUserId);

        followRepository.findByFromAndTo(from, to).ifPresent(
                m->{throw new IllegalStateException("이미 팔로우하고 있는 회원입니다.");}
        );

        Follow follow = new Follow();
        follow.setFrom(from);
        follow.setTo(to);

        followRepository.save(follow);

        if(to.isFollowNoti())
            eventPublisher.publishEvent(new NewFollowerEvent(from, to));

        return follow.getId();
    }

    @Override
    public void unfollow(String userId, String targetUserId) {
        Follow follow = followRepository.findByUserIdAndTargetUserId(userId, targetUserId).orElseThrow(
                ()->{ throw new ElementNotFoundException("Follow", "follow_Id"); }
        );
        followRepository.delete(follow);
    }

    @Override
    public Page<SimpleUserInfoDto> findFollowingList(String currentUserId, String userId, Pageable pageable) {
        User currentUser = getUser(currentUserId);
        User user = getUser(userId);

        List<User> currentUserFollowings = followRepository.findByFrom(currentUser).stream()
                .map( follow -> {return follow.getTo();})
                .collect(Collectors.toList());

        Page<SimpleUserInfoDto> result = followRepository.findByFrom(user, pageable).map(
                follow -> {return new SimpleUserInfoDto(follow.getTo(), currentUserFollowings.contains(follow.getTo()));}
        );

        return result;
    }

    @Override
    public Page<SimpleUserInfoDto> findFollowerList(String currentUserId, String userId, Pageable pageable) {
        User currentUser = getUser(currentUserId);
        User user = getUser(userId);

        List<User> currentUserFollowings = followRepository.findByFrom(currentUser).stream()
                .map( follow -> {return follow.getTo();})
                .collect(Collectors.toList());

        Page<SimpleUserInfoDto> result = followRepository.findByTo(user, pageable).map(
                follow -> {return new SimpleUserInfoDto(follow.getFrom(), currentUserFollowings.contains(follow.getFrom()));}
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
