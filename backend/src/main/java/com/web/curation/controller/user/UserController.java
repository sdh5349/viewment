package com.web.curation.controller.user;

import com.web.curation.commons.ErrorResponse;
import com.web.curation.commons.PageRequest;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.dto.user.UserPageDto;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.security.CustomUserDetails;
import com.web.curation.service.user.FollowService;
import com.web.curation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final FollowService followService;
    private final UserService userService;


    @GetMapping("/{userId}/page")
    public ResponseEntity<?> getUserPage(@PathVariable("userId") String userId, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserPageDto userPage = userService.findUserPageById(currentUserId, userId);
        return ResponseEntity.ok().body(userPage);
    }


    /**
     * 팔로잉 관련 메소드
     */

    @GetMapping("/{userId}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable("userId") String userId, PageRequest pageable, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        List<SimpleUserInfoDto> result = followService.findFollowingsByUserId(currentUserId, userId, pageable.of());

        return ResponseEntity.ok().body(new PageImpl<SimpleUserInfoDto>(result));
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable("userId") String userId, PageRequest pageable, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        List<SimpleUserInfoDto> result = followService.findFollowersByUserId(currentUserId, userId, pageable.of());
        return ResponseEntity.ok().body(new PageImpl<SimpleUserInfoDto>(result));
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> follow(@PathVariable("userId") String userId, @RequestBody Map<String, String> map){
        followService.follow(userId, map.get("targetUserId"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/followings/{targetUserId}")
    public ResponseEntity<?> unfollow(@PathVariable("userId") String userId, @PathVariable("targetUserId") String targetUserId){
        followService.unfollow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/followers/{targetUserId}")
    public ResponseEntity<?> removeFollowers(@PathVariable("userId") String userId, @PathVariable("targetUserId") String targetUserId){
        followService.unfollow(targetUserId, userId);
        return ResponseEntity.ok().build();
    }

}
