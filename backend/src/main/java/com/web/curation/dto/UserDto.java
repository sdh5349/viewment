package com.web.curation.dto;

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
@RequiredArgsConstructor
public class UserDto {
    private String uid;
    private String email;
    private String nickname;
    private UserRole role;
    private Image profileImage;
    private int sizeofFollowings;
    private int sizeofFollowers;
    private boolean isFollowed;

    public UserDto(User user){
        this.uid = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        //this.profileImage = user.getProfileImage();
//        this.sizeofFollowings = user.getFollowings().size();
//        this.sizeofFollowers = user.getFollowers().size();
    }

    public User convert(){
        User user = new User();
        user.setId(uid);
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
