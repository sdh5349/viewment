package com.web.curation.repository.user;

import com.web.curation.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>{

    public Optional<User> findByEmail(String email);
    public Page<User> findByNicknameContaining(String nickname, Pageable pageable);

    @Modifying
    @Query("delete from Follow f where f.from = :del_user or f.to = :del_user")
    public void deleteAssociatedFollow(@Param("del_user") User delUser);

}
