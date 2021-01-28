package com.web.curation.repository.follow;

import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import com.web.curation.exceptions.UserNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class FollowRepositoryImpl implements FollowRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Long follow(String userId, String targetUserId) {
        User user = em.find(User.class, userId);
        User targetUser = em.find(User.class, targetUserId);
        if(user == null || targetUser == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        Follow f = new Follow();
        f.setFrom(user);
        f.setTo(targetUser);
        em.persist(f);
        return f.getId();
    }

    @Override
    public void unfollow(String userId, String targetUserId) {
        Optional<Follow> follow = em.createQuery("select f from Follow f where f.from.id = :userId and f.to.id= :targetUserId", Follow.class)
                .setParameter("userId", userId)
                .setParameter("targetUserId", targetUserId).getResultStream().findAny();

        follow.ifPresent(f->{
            em.remove(f);
        });
    }

    @Override
    public List<User> findFollowers(String userId) {
        User user = em.find(User.class, userId);
        if(user == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        final String JPQL = "select f.from from Follow f join f.to  where f.to.id = :userId";

        return em.createQuery(JPQL, User.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public List<User> findFollowings(String userId) {
        User user = em.find(User.class, userId);
        if(user == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        final String JPQL = "select f.to from Follow f join f.from where f.from.id = :userId";

        return em.createQuery(JPQL, User.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public boolean isFollowed(String fromUserId, String toUserId) {
        final String JPQL = "select count(f) from Follow f join f.from where f.from.id = :fromUserId and f.to.id = :toUserId";
        Long count = (Long)em.createQuery(JPQL)
                .setParameter("fromUserId", fromUserId)
                .setParameter("toUserId", toUserId)
                .getSingleResult();
        return !count.equals(0L);
    }
}
