package com.web.curation.dto.notification;

import com.web.curation.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserNotiDto {

    private boolean followNoti;
    private boolean memoryNoti;
    private boolean likeNoti;

    public UserNotiDto(User user) {
        this.memoryNoti = user.isMemoryNoti();
        this.followNoti = user.isFollowNoti();
        this.likeNoti = user.isLikeNoti();
    }

}
