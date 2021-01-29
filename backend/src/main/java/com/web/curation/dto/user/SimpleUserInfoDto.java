package com.web.curation.dto.user;

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
 * 기존 FollowDto의 이름을 SimpleUserInfoDto로 변경함.
 * 이유는  팔로우/팔로워 리스트에서 뿐만 아니라
 * 유저 검색 결과로도 이 Dto와 동일한 구성를 보여줘야 하기 때문에
 * 다양하게 사용할 수 있는 Dto 클래스로 변경  2021-01-26
 **/

@Getter
@Setter
public class SimpleUserInfoDto {
    private String userId;
    private String nickname;
    private Image profileImage;
    private boolean isFollowed;

    public SimpleUserInfoDto(User user){
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    public SimpleUserInfoDto(User user, boolean isFollowed){
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.isFollowed = isFollowed;
    }
}
