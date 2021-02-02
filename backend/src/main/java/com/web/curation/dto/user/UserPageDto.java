package com.web.curation.dto.user;

import com.web.curation.domain.Image;
import com.web.curation.domain.User;
import lombok.Getter;
import lombok.Setter;

/**
 * com.web.curation.dto.user
 * UserPageDto.java
 * @date    2021-01-25 오후 5:46
 * @author  김종성
 *
 * @변경이력
 **/

@Getter
@Setter
public class UserPageDto {
    private String userId;
    private String email;
    private String nickname;
    private String message;
    private Image profileImage;
    private int countFollowings;
    private int countFollowers;
    private int countMemories;
    private int countArticles;
    private boolean isFollowed;

    public UserPageDto(){}

    public UserPageDto(User user, int countFollowers, int countFollowings, int countMemories, int countArticles, boolean isFollowed){
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.message = user.getMessage();
        this.profileImage = user.getProfileImage();
        this.countFollowers = countFollowers;
        this.countFollowings = countFollowings;
        this.countMemories = countMemories;
        this.isFollowed = isFollowed;
    }
}
