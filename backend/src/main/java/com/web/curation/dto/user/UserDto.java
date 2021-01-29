package com.web.curation.dto.user;

import com.web.curation.domain.Image;
import com.web.curation.domain.User;
import com.web.curation.domain.UserRole;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Getter
@Setter
public class UserDto {
    private String userId;
    private String email;
    private String nickname;
    private UserRole role;
    private Image profileImage;

    public UserDto(){}

    public UserDto(User user){
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        //this.profileImage = user.getProfileImage();
//        this.sizeofFollowings = user.getFollowings().size();
//        this.sizeofFollowers = user.getFollowers().size();
    }

    public User convert(){
        User user = new User();
        user.setId(userId);
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
