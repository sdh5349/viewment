package com.web.curation.service;

import com.web.curation.domain.User;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.user.AccountService;
import com.web.curation.service.user.FollowService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired
    FollowService followService;

    @Autowired
    AccountService accountService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        User u1 = new User("aaa", "A", "A");
        User u2 = new User("bbb", "B", "B");
        User u3 = new User("ccc", "C", "C");
        User u4 = new User("ddd", "D", "D");

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);


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
    void 내가_팔로우하지_않는_사용자가_포함된_팔로워_목록() throws Exception{
        //given

        //when
        Page<SimpleUserInfoDto> followers = followService.findFollowerList("aaa", "aaa", PageRequest.of(0, 10));
        //then
        int size = followers.getContent().stream()
                .filter(f -> !f.isFollowed())
                .collect(Collectors.toList()).size();

        Assertions.assertThat(size).isSameAs(1);
    }

    @Test
    void 유저_삭제시_팔로워_목록_변화_성공() throws Exception{

        String from = "ddd";
        String to = "aaa";

        accountService.delete(from);

        Page<SimpleUserInfoDto> list = followService.findFollowerList("aaa", "aaa", PageRequest.of(0, 10));

        Assertions.assertThat(list.getContent().size()).isEqualTo(2);
    }
}
