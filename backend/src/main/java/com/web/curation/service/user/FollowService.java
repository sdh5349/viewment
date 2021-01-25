package com.web.curation.service.user;

import com.web.curation.dto.FollowDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * com.web.curation.service.user
 * FollowService.java
 * @date    2021-01-25 오전 11:19
 * @author  김종성
 *
 * @변경이력
 **/
public interface FollowService{

    public Long follow(String uid, String targetUid);
    public void unfollow(String uid, String targetUid);
    public List<FollowDto> findFollowingsByUserId(String currentUserUid, String uid);
    public List<FollowDto> findFollowersByUserId(String currentUserUid, String uid);
    public int getSizeofFollowers(String uid);
    public int getSizeofFollowings(String uid);
}
