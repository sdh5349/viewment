package com.web.curation.controller.user;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.curation.commons.ErrorResponse;
import com.web.curation.commons.PageRequest;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.dto.user.UserPageDto;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.security.CustomUserDetails;
import com.web.curation.service.user.FollowService;
import com.web.curation.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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

@Api(tags = {"2. User"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final FollowService followService;
    private final UserService userService;

    @ApiOperation(value = "회원 상세 페이지 가져오기")
    @GetMapping("/{userId}/page")
    public ResponseEntity<UserPageDto> getUserPage(@PathVariable("userId") String userId, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        UserPageDto userPage = userService.findUserPageById(currentUserId, userId);
        return ResponseEntity.ok().body(userPage);
    }

    /**
     * 팔로잉 관련 메소드
     */
    @ApiOperation(value = "회원 팔로잉 목록 가져오기")
    @GetMapping("/{userId}/followings")
    public ResponseEntity<Page<SimpleUserInfoDto>> getFollowings(@PathVariable("userId") String userId, PageRequest pageable, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        Page<SimpleUserInfoDto> result = followService.findFollowingList(currentUserId, userId, pageable.of());

        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "회원 팔로워 목록 가져오기")
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Page<SimpleUserInfoDto>> getFollowers(@PathVariable("userId") String userId, PageRequest pageable, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        Page<SimpleUserInfoDto> result = followService.findFollowerList(currentUserId, userId, pageable.of());
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "회원 팔로잉")
    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> follow(@PathVariable("userId") String userId, @RequestBody Map<String, String> map) {

        try {
            followService.follow(userId, map.get("targetUserId"));
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원 언팔로잉")
    @DeleteMapping("/{userId}/followings/{targetUserId}")
    public ResponseEntity<?> unfollow(@PathVariable("userId") String userId, @PathVariable("targetUserId") String targetUserId){
        followService.unfollow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원의 팔로워 중에서 한 회원을 삭제")
    @DeleteMapping("/{userId}/followers/{targetUserId}")
    public ResponseEntity<?> removeFollowers(@PathVariable("userId") String userId, @PathVariable("targetUserId") String targetUserId){
        followService.unfollow(targetUserId, userId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "닉네임으로 회원 검색")
    @GetMapping("/like/{nickname}")
    public ResponseEntity<Page<SimpleUserInfoDto>> searchNickname(@PathVariable("nickname") String nickname, Authentication authentication, PageRequest pageable){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        Page<SimpleUserInfoDto> result = userService.findUsersByNickname(currentUserId, nickname, pageable.of());
        return ResponseEntity.ok().body(result);
    }
}
