package com.web.curation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.domain.User;
import com.web.curation.dto.user.AccountDto;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.user.AccountService;
import com.web.curation.service.user.FollowService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * com.web.curation.controller
 * UserControllerTest.java
 * @date    2021-02-01 오후 4:12
 * @author  김종성
 *
 * @변경이력
 **/

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId("a");
        accountDto.setEmail("a");
        accountDto.setNickname("nickname");

        accountService.join(accountDto);
    }

    @Test
    void 회원가입() throws Exception{
        //given
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId("aaa");
        accountDto.setEmail("aaa@aaa");
        accountDto.setNickname("nickname");

        String content = mapper.writeValueAsString(accountDto);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/accounts")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isCreated());
    }

    @Test
    void 중복이메일_회원가입_실패() throws Exception{
        //given
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId("aaa");
        accountDto.setEmail("a");
        accountDto.setNickname("nickname");

        String content = mapper.writeValueAsString(accountDto);

        //when

        ResultActions result = mockMvc.perform(post("/api/v1/accounts")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest());
    }


    @WithMockUser(username = "a", password = "")
    @Test
    void 유저페이지_가져오기() throws Exception{
        //given
        String userId = "a";
        //when
        ResultActions resultActions =  mockMvc.perform(get("/api/v1/users" +
                "/" + userId +
                "/page"));
        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @WithMockUser(username = "a", password = "")
    @Test
    void 회원정보_수정() throws Exception{
        //given
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId("a");
        accountDto.setNickname("nickname");
        accountDto.setMessage("message");

        String content = mapper.writeValueAsString(accountDto);

        //when
        ResultActions result = mockMvc.perform(patch("/api/v1/accounts" +
                "/" + "a")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());

        User findUser = userRepository.findById(accountDto.getUserId()).get();
        Assertions.assertThat(findUser.getMessage()).isEqualTo(accountDto.getMessage());
    }

    @WithMockUser(username = "a", password = "")
    @Test
    void 회원탈퇴() throws Exception{
        //given
        String userId = "a";
        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/accounts" +
                        "/" + "a"));

        //then
        result.andExpect(status().isOk());

        User findUser = userRepository.findById("a").orElse(null);
        Assertions.assertThat(findUser).isNull();
    }

    @Test
    void 토큰_인증_실패() throws Exception{
        //given

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/users/a/page")
                .header("X-Authorization-Firebase", "sdadsadasd"));
        //then
        result.andExpect(status().isUnauthorized());
    }

}
