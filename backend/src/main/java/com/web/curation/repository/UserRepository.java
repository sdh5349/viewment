package com.web.curation.repository;

import com.web.curation.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    public User save(User user);
    public void update(User user);

    public void delete(String id);

    public Optional<User> findById(String id);
    public Optional<User> findByEmail(String email);

    public List<User> findByNickName(String nickname);

    public List<User> findAll();
}
