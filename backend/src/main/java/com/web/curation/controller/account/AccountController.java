package com.web.curation.controller.account;

import com.web.curation.commons.ErrorResponse;
import com.web.curation.dto.UserDto;
import com.web.curation.exceptions.UserDuplicateException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.service.user.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/*@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })*/

@Api(tags = {"1. Account"})
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    /*private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;*/
    private final AccountService accountService;

    @ApiOperation(value = "회원 가입", response = String.class)
    @ApiResponse(code = 201, message = "created")
    @PostMapping("")
    public ResponseEntity<?> join(@RequestBody UserDto userDto){
        accountService.join(userDto);
        return new ResponseEntity<String>("Created", HttpStatus.CREATED);
    }

    @PatchMapping("")
    public ResponseEntity<?> modify(@RequestBody UserDto userDto){
        String modifyId = accountService.modify(userDto);
        return new ResponseEntity<String>("Modified: " + modifyId, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id){
        String deleteId = accountService.delete(id);
        return new ResponseEntity<String>("Deleted: " + deleteId, HttpStatus.OK);
    }
//    @PostMapping("/authenticate")
//    @ApiOperation(value="인증 토큰 획득")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        final String jwt = "Bearer " + jwtUtil.generateToken(userDetails);
//
//        return ResponseEntity.ok(new AuthenticationResponseDto(jwt));
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
//        return new ResponseEntity<>(new ErrorResponse(
//                "인증되지 않은 사용자입니다", "bad.credential.exception"), HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(UserDuplicateException.class)
    public ResponseEntity<?>  handleUserDuplicateException(UserDuplicateException e){
        final String msg  = "[ " + e.getEmail() + "] 은 중복된 이메일입니다";
        final String code = "duplicated.email.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?>  handleUserNotFoundException(UserNotFoundException e){
        final String msg  = e.getValue();
        final String code = "nonexistence.user.exception";
        ErrorResponse errorResponse = new ErrorResponse(msg, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
