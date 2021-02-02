package com.web.curation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.config.FirebaseConfig;
import com.web.curation.domain.User;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.user.AccountService;
import com.web.curation.service.user.FollowService;
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

/**
 * com.web.curation.controller
 * FollowControllerTest.java
 * @date    2021-02-01 오후 3:09
 * @author  김종성
 *
 * @변경이력
 **/

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FollowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FollowService followService;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        for(int i = 0; i<10; i++){
            User user = new User();
            user.setId((char)(i+'a') + "");
            user.setNickname((char)(i+'a') + "");
            user.setEmail((char)(i+'a') + "");
            userRepository.save(user);
        }
        followService.follow("a", "b");
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

        User targetUser = userRepository.findById(targetUserId).get();

        Assertions.assertThat(followRepository.findByUserIdAndTargetUserId(userId, targetUserId).get().getTo())
                .isEqualTo(targetUser);
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

        Assertions.assertThat(followRepository.findByUserIdAndTargetUserId(userId, targetUserId).isPresent())
                .isFalse();
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

        Assertions.assertThat(followRepository.findByUserIdAndTargetUserId(targetUserId, userId).isPresent())
                .isFalse();
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
