package com.web.curation.follow;

import com.web.curation.domain.User;
import com.web.curation.dto.FollowDto;
import com.web.curation.service.user.FollowService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class FollowServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    FollowService followService;

    @BeforeEach
    void setUp(){
        User u1 = new User("aaa", "A", "A");
        User u2 = new User("bbb", "B", "B");
        User u3 = new User("ccc", "C", "C");
        User u4 = new User("ddd", "D", "D");

        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.persist(u4);

        followService.follow("aaa", "bbb");
        followService.follow("aaa", "ccc");
        followService.follow("bbb", "aaa");
        followService.follow("ccc", "aaa");
        followService.follow("ddd", "aaa");
        /**
         *  A -> B, C
         *  B, C, D -> A
         */
    }

    @Test
    void 내가_팔로우하지_않는_사용자가_포함된_목록() throws Exception{
        //given

        //when
        List<FollowDto> followers = followService.findFollowersByUserId("aaa", "aaa");
        //then
        int size = followers.stream()
                .filter(f -> !f.isFollowed())
                .collect(Collectors.toList()).size();

        Assertions.assertThat(size).isSameAs(1);
    }

}
