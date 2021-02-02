package com.web.curation.user;

import com.web.curation.domain.User;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.service.user.FollowService;
import com.web.curation.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp(){
        User u1 = new User("aaa", "A", "A");
        User u2 = new User("bbb", "B", "B");
        User u3 = new User("ccc", "C", "C");
        User u4 = new User("ddd", "D", "D");
        User u5 = new User("eee", "E", "AD");
        User u6 = new User("fff", "F", "AA");

        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.persist(u4);
        em.persist(u5);
        em.persist(u6);

        followService.follow("aaa", "bbb");
        followService.follow("aaa", "ccc");
        followService.follow("bbb", "aaa");
        followService.follow("ccc", "aaa");
        followService.follow("ddd", "aaa");
    }

    @Test
    void 닉네임_검색_성공() throws Exception{
        //given

        //when
        Page<SimpleUserInfoDto> list = userService.findUsersByNickname("bbb", "A", PageRequest.of(0, 10));
        //then

        Assertions.assertThat((int)list.getContent().size()).isEqualTo(3);
    }

    @Test
    void 닉네임_검색_페이징_성공() throws Exception{
        //given

        //when
        Page<SimpleUserInfoDto> list = userService.findUsersByNickname("bbb", "A", PageRequest.of(1, 2));
        //then

        Assertions.assertThat((int)list.getContent().size()).isEqualTo(1);
    }

}
