package com.web.curation.dto;

import com.web.curation.domain.Image;
import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * com.web.curation.dto
 * FollowDto.java
 * @date    2021-01-25 오전 11:19
 * @author  김종성
 *
 * @변경이력
 **/

@Getter
@Setter
public class FollowDto {
    private String uid;
    private String nickname;
    private Image profileImage;
    private boolean isFollowed;

    public FollowDto(User user){
        this.uid = user.getId();
        this.nickname = user.getNickname();
        //this.profileImage = user.getProfileImage();
    }

    public FollowDto(User user, boolean isFollowed){
        this.uid = user.getId();
        this.nickname = user.getNickname();
        //this.profileImage = user.getProfileImage();
        this.isFollowed = isFollowed;
    }
}
