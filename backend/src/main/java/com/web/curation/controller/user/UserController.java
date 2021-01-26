package com.web.curation.controller.user;

import com.web.curation.commons.ErrorResponse;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.dto.user.UserPageDto;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.security.CustomUserDetails;
import com.web.curation.service.user.FollowService;
import com.web.curation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final FollowService followService;
    private final UserService userService;


    @GetMapping("/{userId}/page")
    public ResponseEntity<?> getUserPage(@PathVariable("userId") String userId, Authentication authentication){
        final String currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUsername();
        UserPageDto userPage = userService.findUserPageById(currentUserId, userId);
        return ResponseEntity.ok().body(userPage);
    }


    /**
     * 팔로잉 관련 메소드
     */

    @GetMapping("/{userId}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable("userId") String userId, Pageable pageable, Authentication authentication){
        final String currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUsername();
        List<SimpleUserInfoDto> result = followService.findFollowingsByUserId(currentUserId, userId, pageable);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable("userId") String userId, Pageable pageable, Authentication authentication){
        final String currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUsername();
        List<SimpleUserInfoDto> result = followService.findFollowersByUserId(currentUserId, userId, pageable);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> follow(@PathVariable("userId") String userId, @RequestBody String targetUserId){
        followService.follow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/follows/{targetUserId}")
    public ResponseEntity<?> unfollow(@PathVariable("userId") String userId, @PathVariable("targetUserId") String targetUserId){
        followService.follow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?>  handleUserNotFoundException(UserNotFoundException e){
        final String msg  = e.getValue();
        final String code = "nonexistence.user.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
