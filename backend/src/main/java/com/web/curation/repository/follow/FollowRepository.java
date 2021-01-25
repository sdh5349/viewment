package com.web.curation.repository.follow;

import com.web.curation.domain.User;
import java.util.List;

public interface FollowRepository {
    public Long follow(String uid, String targetUid);
    public void unfollow(String uid, String targetUid);

    public List<User> findFollowers(String uid);
    public List<User> findFollowings(String uid);
}
