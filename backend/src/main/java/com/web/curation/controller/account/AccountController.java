package com.web.curation.controller.account;

import com.web.curation.commons.ErrorResponse;
import com.web.curation.dto.user.AccountDto;
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



@Api(tags = {"1. Account"})
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @ApiOperation(value = "회원 가입")
    @ApiResponse(code = 201, message = "created")
    @PostMapping("")
    public ResponseEntity<String> join(@RequestBody AccountDto userDto){
        accountService.join(userDto);
        return new ResponseEntity<String>("Created", HttpStatus.CREATED);
    }


    @ApiOperation(value = "회원 계정 수정")
    @PatchMapping("/{userId}")
    public ResponseEntity<String> modify(@PathVariable("userId") String userId, @RequestBody AccountDto userDto){
        userDto.setUserId(userId);
        String modifyId = accountService.modify(userDto);
        return new ResponseEntity<String>("Modified: " + modifyId, HttpStatus.OK);
    }


    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@PathVariable(value = "userId") String userId){
        accountService.delete(userId);
        return new ResponseEntity<String>("Deleted: " + userId, HttpStatus.OK);
    }

    @ApiOperation(value = "중복 이메일 확인")
    @GetMapping("/{email}/check")
    public ResponseEntity<?> checkDuplicatedEmail(@PathVariable(value = "email") String email){
        accountService.validateDuplicateUser(email);
        return ResponseEntity.ok().build();
    }
}
