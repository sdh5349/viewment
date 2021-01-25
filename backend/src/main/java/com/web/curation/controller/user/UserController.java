package com.web.curation.controller.user;

import com.web.curation.commons.ErrorResponse;
import com.web.curation.dto.UserDto;
import com.web.curation.exceptions.FirebaseInvalidTokenException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.UserRepository;
import com.web.curation.service.firebase.FirebaseAccountService;
import com.web.curation.service.user.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/users")
public class UserController {
    //private FirebaseAccountService firebaseAccountService;
    private final FollowService followService;


    @GetMapping("/{uid}")
    public ResponseEntity<?> getUserInfo(@PathVariable("uid") String uid){

        return null;
    }

    @GetMapping("/{uid}/page")
    public ResponseEntity<?> getUserPage(@PathVariable("uid") String uid, HttpServletRequest request){

        return null;
    }


    /**
     * 팔로잉 관련 메소드
     */

    @GetMapping("/{uid}/followings")
    public ResponseEntity<?> getFollowings(@PathVariable("uid") String uid, HttpServletRequest request){
        final String ftoken = request.getHeader("X-Authorization-Firebase");

        return null;
    }

    @GetMapping("/{uid}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable("uid") String uid, HttpServletRequest request){
        final String ftoken = request.getHeader("X-Authorization-Firebase");

        return null;
    }

    @PostMapping("/{uid}/follows")
    public ResponseEntity<?> follow(@PathVariable("uid") String uid, @RequestBody String targetUid, HttpServletRequest request){
        final String ftoken = request.getHeader("X-Authorization-Firebase");

        return null;
    }



    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?>  handleUserNotFoundException(UserNotFoundException e){
        final String msg  = e.getValue();
        final String code = "nonexistence.user.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FirebaseInvalidTokenException.class)
    public ResponseEntity<?>  handleFirebaseInvalidTokenException(FirebaseInvalidTokenException e){
        final String msg  = e.getMessage();
        final String code = "Invalidated.token.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
