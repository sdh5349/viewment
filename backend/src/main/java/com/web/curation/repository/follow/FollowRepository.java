package com.web.curation.repository.follow;

import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * com.web.curation.repository.follow
 * FollowRepository.java
 * @date    2021-02-01 오후 1:39
 * @author  김종성
 *
 * @변경이력
 **/

public interface FollowRepository extends JpaRepository<Follow, Long> {

    public List<Follow> findByFrom(User from);
    public List<Follow> findByTo(User to);

    public Page<Follow> findByFrom(User from, Pageable pageable);
    public Page<Follow> findByTo(User to, Pageable pageable);

    //user가 target user를 팔로우 하고 있는지
    @Query("select f from Follow f where f.from.id = :userId and f.to.id = :targetUserId")
    public Optional<Follow> findByUserIdAndTargetUserId(@Param("userId") String userId, @Param("targetUserId") String targetUserId);

    Long countByFrom(User from);
    Long countByTo(User to);
}
