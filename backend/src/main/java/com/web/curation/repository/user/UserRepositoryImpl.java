package com.web.curation.repository.user;

import com.web.curation.domain.User;
import com.web.curation.exceptions.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public void update(User user) {
    }

    @Override
    public void delete(String id) {
        User deleteUser = em.find(User.class, id);
        if(deleteUser == null) throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        em.remove(deleteUser);
    }

    @Override
    public Optional<User> findById(String id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :email", User.class).setParameter("email", email)
                .getResultStream().findAny();
    }

    @Override
    public List<User> findByNickName(String nickname, Pageable pageable) {
        return em.createQuery("select u from User u where u.nickname like '%'||:nickname||'%'", User.class)
                .setParameter("nickname", nickname)
                .setFirstResult(pageable.getPageNumber()* pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }
}
