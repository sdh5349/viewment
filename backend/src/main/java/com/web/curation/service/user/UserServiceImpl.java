package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.dto.user.UserPageDto;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public UserPageDto findUserPageById(String currentUserId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->{throw new UserNotFoundException("사용자가 존재하지 않습니다.");});

        boolean isFollowed = followRepository.isFollowed(currentUserId, userId);

        int countFollowings = followRepository.findFollowings(userId).size();
        int countFollowers = followRepository.findFollowers(userId).size();

        return new UserPageDto(user, countFollowers, countFollowings,0, isFollowed);
    }

    @Override
    public SimpleUserInfoDto findUserByEmail(String currentUserId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->{throw new UserNotFoundException("사용자가 존재하지 않습니다.");});

        boolean isFollowed = followRepository.isFollowed(currentUserId, user.getId());
        return new SimpleUserInfoDto(user, isFollowed);
    }

    @Override
    public List<SimpleUserInfoDto> findUsersByNickname(String currentUserId, String nickname, Pageable pageable) {
        List<User> users = userRepository.findByNickName(nickname, pageable);
        List<User> followingsCurrentUser = followRepository.findFollowings(currentUserId);

        List<SimpleUserInfoDto> result = users.stream().map(
                user -> {
                    return new SimpleUserInfoDto(user, followingsCurrentUser.contains(user));
                }
        ).collect(Collectors.toList());
        return result;
    }
}
