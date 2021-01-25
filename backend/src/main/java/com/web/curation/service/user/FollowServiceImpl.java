package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.FollowDto;
import com.web.curation.repository.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;

    @Override
    public Long follow(String uid, String targetUid) {
        return followRepository.follow(uid, targetUid);
    }

    @Override
    public void unfollow(String uid, String targetUid) {
        followRepository.unfollow(uid, targetUid);
    }

    @Override
    public List<FollowDto> findFollowingsByUserId(String currentUserUid, String uid) {
        List<User> currentUserFollowings = followRepository.findFollowings(currentUserUid);

        List<FollowDto> result = followRepository.findFollowings(uid).stream()
                .map( user -> {
                    return new FollowDto(user, currentUserFollowings.contains(user));
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<FollowDto> findFollowersByUserId(String currentUserUid, String uid) {
        List<User> currentUserFollowings = followRepository.findFollowings(currentUserUid);

        List<FollowDto> result = followRepository.findFollowers(uid).stream()
                .map( user -> {
                    return new FollowDto(user, currentUserFollowings.contains(user));
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public int getSizeofFollowers(String uid) {
        return 0;
    }

    @Override
    public int getSizeofFollowings(String uid) {
        return 0;
    }
}
