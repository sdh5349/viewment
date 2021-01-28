package com.web.curation.repository.follow;


import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
public class FollowRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    FollowRepository followRepository;

    @BeforeEach
    void setUp(){
        User u1 = new User();
        u1.setId("aaa");
        u1.setNickname("user1");
        u1.setEmail("aaa@aaa");

        User u2 = new User();
        u2.setId("bbb");
        u2.setNickname("user2");
        u2.setEmail("bbb@bbb");

        User u3 = new User();
        u3.setId("ccc");
        u3.setNickname("user3");
        u3.setEmail("ccc@ccc");


        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
    }

    @Test
    void 팔로우_성공() throws Exception{
        //given
        Long fid = followRepository.follow("aaa", "bbb");

        //when
        Follow follow = em.find(Follow.class, fid);

        //then
        Assertions.assertThat(follow.getId()).isEqualTo(fid);
    }

    @Test
    void 언팔로우_성공() throws Exception{
        //given
        Long fid = followRepository.follow("aaa", "bbb");
        followRepository.unfollow("aaa", "bbb");

        Assertions.assertThat(em.find(Follow.class, fid)).isEqualTo(null);
    }


    @Test
    void 팔로잉_목록_가져오기() throws Exception{
        //given
        Long fid = followRepository.follow("aaa", "bbb");
        Long fid2 = followRepository.follow("aaa", "ccc");
        //when
        List<User> followings = followRepository.findFollowings("aaa");
        //then
        Assertions.assertThat(followings.size()).isSameAs(2);
    }

    @Test
    void 팔로워_목록_가져오기() throws Exception{
        //given
        Long fid = followRepository.follow("aaa", "bbb");
        Long fid2 = followRepository.follow("aaa", "ccc");
        //when
        List<User> followings = followRepository.findFollowers("bbb");
        //then
        Assertions.assertThat(followings.size()).isSameAs(1);
    }

    @Test
    void A가B를_팔로우() throws Exception{
        //given
        Long fid = followRepository.follow("aaa", "bbb");
        Long fid2 = followRepository.follow("aaa", "ccc");
        //when
        boolean result = followRepository.isFollowed("aaa", "bbb");
        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void C가B를_팔로우_실패() throws Exception{
        //given
        Long fid = followRepository.follow("aaa", "bbb");
        Long fid2 = followRepository.follow("aaa", "ccc");
        //when
        boolean result = followRepository.isFollowed("ccc", "bbb");
        //then
        Assertions.assertThat(result).isFalse();
    }

}
