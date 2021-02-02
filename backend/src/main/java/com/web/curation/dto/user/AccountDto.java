package com.web.curation.dto.user;

import com.web.curation.domain.Image;
import com.web.curation.domain.User;
import com.web.curation.domain.UserRole;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
/**
 * com.web.curation.dto.user
 * AccountDto.java
 * @date    2021-02-01 오후 5:02
 * @author  김종성
 *
 * @변경이력
 * 기존 UserDto를 AccountDto로 변경(역할을 명확하게 하기 위해)
 * 회원가입, 계정 정보 수정시 자용
 **/
@Getter
@Setter
public class AccountDto {
    private String userId;
    private String email;
    private String nickname;
    private String message;
    private UserRole role;
    private Image profileImage;


}
