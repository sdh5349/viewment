package com.web.curation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.config.FirebaseConfig;
import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.dto.memory.MemoryDto;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.memory.MemoryService;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemoryService memoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PinRepository pinRepository;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        User user1 = new User();
        user1.setId("a");
        user1.setNickname("aaa");
        user1.setEmail("aaa@aaa");
        userRepository.save(user1);

        MemoryDto memoryDto = new MemoryDto();
        memoryDto.setLat(1.0000);
        memoryDto.setLng(2.0000);
        memoryDto.setName("memory");memoryDto.setRadius(10);
        memoryService.createWithoutPin("a", memoryDto);

    }

    @Test
    @WithMockUser(username = "userId", password = "")
    void 기억하기_가져오기() throws Exception{
        //given
        String userId = "a";

        //when
        ResultActions resultActions =  mockMvc.perform(get("/api/v1/users" +
                "/" + userId +
                "/memories"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
