package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.repository.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<SimpleUserInfoDto> findFollowingsByUserId(String currentUserUid, String uid, Pageable pageable) {
        List<User> currentUserFollowings = followRepository.findFollowings(currentUserUid);
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        List<SimpleUserInfoDto> result = followRepository.findFollowings(uid).stream()
                .map( user -> {
                    return new SimpleUserInfoDto(user, currentUserFollowings.contains(user));
                })
                .collect(Collectors.toList());
        int start = page*size;
        int end =  page*size+size;
        end = end< result.size() ? end : result.size();
        return result.subList(start, end);
    }

    @Override
    public List<SimpleUserInfoDto> findFollowersByUserId(String currentUserUid, String uid, Pageable pageable) {
        List<User> currentUserFollowings = followRepository.findFollowings(currentUserUid);
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        List<SimpleUserInfoDto> result = followRepository.findFollowers(uid).stream()
                .map( user -> {
                    return new SimpleUserInfoDto(user, currentUserFollowings.contains(user));
                })
                .collect(Collectors.toList());
        int start = page*size;
        int end = page*size+size<result.size() ? page*size + size : result.size();
        return result.subList(start, end);
    }

    @Override
    public int getSizeofFollowers(String uid) {
        return followRepository.findFollowers(uid).size();
    }

    @Override
    public int getSizeofFollowings(String uid) {
        return followRepository.findFollowings(uid).size();
    }
}
