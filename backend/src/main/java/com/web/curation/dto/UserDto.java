package com.web.curation.dto;

import com.web.curation.domain.User;
import com.web.curation.domain.UserRole;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String nickname;
    private UserRole role;


    public User convert(){
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setNickname(nickname);
        if(role == null) role = UserRole.USER;
        user.setRole(role);
        return user;
    }

    public User convert(User user){
        if(role != null) user.setRole(role);
        if(nickname != null) user.setNickname(nickname);
        return user;
    }
}
