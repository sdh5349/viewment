package com.web.curation.service.user;

import com.web.curation.dto.user.SimpleUserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowService{

    public Long follow(String userId, String targetUserId);
    public void unfollow(String userId, String targetUserId);
    public Page<SimpleUserInfoDto> findFollowingList(String currentUserId, String userId, Pageable pageable);
    public Page<SimpleUserInfoDto> findFollowerList(String currentUserId, String userId, Pageable pageable);
    public int getSizeofFollowers(String userId);
    public int getSizeofFollowings(String userId);
}
