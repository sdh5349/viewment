package com.web.curation.repository.follow;

import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import com.web.curation.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Long follow(String uid, String targetUid) {
        User user = em.find(User.class, uid);
        User targetUser = em.find(User.class, targetUid);
        if(user == null || targetUser == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        Follow f = new Follow();
        f.setFrom(user);
        f.setTo(targetUser);
        em.persist(f);
        return f.getId();
    }

    @Override
    public void unfollow(String uid, String targetUid) {
        Optional<Follow> follow = em.createQuery("select f from Follow f where f.from.id = :uid and f.to.id= :targetUid", Follow.class)
                .setParameter("uid", uid)
                .setParameter("targetUid", targetUid).getResultStream().findAny();

        follow.ifPresent(f->{
            em.remove(f);
        });
    }

    @Override
    public List<User> findFollowers(String uid) {
        User user = em.find(User.class, uid);
        if(user == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        final String JPQL = "select f.from from Follow f join f.to  where f.to.id = :uid";

        return em.createQuery(JPQL, User.class).setParameter("uid", uid).getResultList();
    }

    @Override
    public List<User> findFollowings(String uid) {
        User user = em.find(User.class, uid);
        if(user == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        final String JPQL = "select f.to from Follow f join f.from where f.from.id = :uid";

        return em.createQuery(JPQL, User.class).setParameter("uid", uid).getResultList();
    }

}
