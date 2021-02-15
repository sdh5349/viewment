package com.web.curation.service.user;

import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.dto.user.UserPageDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.article.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final MemoryRepository memoryRepository;
    private final ArticleRepository articleRepository;

    public User getUser(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->{throw new UserNotFoundException();});
        return user;
    }

    @Override
    public UserPageDto findUserPageById(String currentUserId, String userId) {
        User user = getUser(userId);

        Follow follow = followRepository.findByUserIdAndTargetUserId(currentUserId, userId).orElse(null);

        boolean isFollowed = follow==null?false:true;

        int countFollowings = followRepository.countByFrom(user).intValue();
        int countFollowers = followRepository.countByTo(user).intValue();
        int countMemories = memoryRepository.countByUser(user).intValue();
        int countArticles = articleRepository.countByUser(user).intValue();

        return new UserPageDto(user, countFollowers, countFollowings, countMemories, countArticles, isFollowed);
    }

    @Override
    public SimpleUserInfoDto findUserByEmail(String currentUserId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->{throw new UserNotFoundException();});
        User currentUser = getUser(currentUserId);

        Follow follow = followRepository.findByUserIdAndTargetUserId(currentUserId, user.getId())
                .orElse(null);

        boolean isFollowed = follow==null?false:true;

        return new SimpleUserInfoDto(user, isFollowed);
    }

    @Override
    public Page<SimpleUserInfoDto> findUsersByNickname(String currentUserId, String nickname, Pageable pageable) {
        User currentUser = getUser(currentUserId);

        Page<User> users = userRepository.findByNicknameContaining(nickname, pageable);

        List<User> followingsCurrentUser = followRepository.findByFrom(currentUser).stream()
                .map( follow ->{return follow.getTo();}
        ).collect(Collectors.toList());

        Page<SimpleUserInfoDto> result = users.map(
                user -> {
                    return new SimpleUserInfoDto(user, followingsCurrentUser.contains(user));
                }
        );

        return result;
    }
}
