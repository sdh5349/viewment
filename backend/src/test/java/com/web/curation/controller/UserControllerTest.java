package com.web.curation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.config.FirebaseConfig;
import com.web.curation.domain.User;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.user.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        User user1 = new User();
        user1.setId("a");
        user1.setNickname("aaa");
        user1.setEmail("aaa@aaa");
        userRepository.save(user1);

        User user2 = new User();
        user2.setId("b");
        user2.setNickname("bbb");
        user2.setEmail("bbb@bbb");
        userRepository.save(user2);

        followRepository.follow("a", "b");
    }

    @Test
    @WithMockUser(username = "userId", password = "")
    void 팔로우_성공() throws Exception{
        //given
        String userId = "b";
        String targetUserId = "a";

        Map<String, String> input = new HashMap<>();
        input.put("targetUserId", targetUserId);

        String content = mapper.writeValueAsString(input);

        //when
        ResultActions resultActions =  mockMvc.perform(post("/api/v1/users" +
                "/" + userId +
                "/follow")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk());

        Assertions.assertThat(followRepository.isFollowed(userId, targetUserId)).isTrue();
    }

    @Test
    @WithMockUser(username = "userId", password = "")
    void 언팔로우_성공() throws Exception{
        //given
        String userId = "a";
        String targetUserId = "b";
        //when
        ResultActions resultActions =  mockMvc.perform(delete("/api/v1/users" +
                "/" + userId +
                "/followings" +
                "/" + targetUserId));
        //then
        resultActions
                .andExpect(status().isOk());
        Assertions.assertThat(followRepository.isFollowed(userId, targetUserId)).isFalse();
    }

    @Test
    @WithMockUser(username = "userId", password = "")
    void 팔로워_삭제_성공() throws Exception{
        //given
        String userId = "b";
        String targetUserId = "a";
        //when
        ResultActions resultActions =  mockMvc.perform(delete("/api/v1/users" +
                "/" + userId +
                "/followers" +
                "/" + targetUserId));
        //then
        resultActions
                .andExpect(status().isOk());

        Assertions.assertThat(followRepository.isFollowed(targetUserId, userId)).isFalse();
    }

    @Test
    @WithMockUser(username = "a", password = "")
    void 팔로우_목록_가져오기() throws Exception{
        //given
        MultiValueMap<String, String> pageable = new LinkedMultiValueMap<>();
        pageable.add("size", "10");
        pageable.add("page", "0");

        //when
        ResultActions resultActions =  mockMvc.perform(get("/api/v1/users" +
                "/" + "a"+
                "/followings")
                .queryParams(pageable));
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "b", password = "")
    void 팔로워_목록_가져오기() throws Exception{
        //given
        MultiValueMap<String, String> pageable = new LinkedMultiValueMap<>();
        pageable.add("size", "10");
        pageable.add("page", "0");

        //when
        ResultActions resultActions =  mockMvc.perform(get("/api/v1/users" +
                "/" + "b"+
                "/followers")
                .queryParams(pageable));
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }
}
