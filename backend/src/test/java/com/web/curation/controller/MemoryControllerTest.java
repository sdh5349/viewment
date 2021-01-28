package com.web.curation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.service.memory.MemoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MemoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemoryService memoryService;

    @Autowired
    ObjectMapper mapper;

    @Test
    @WithMockUser(username = "userId", password = "")
    void 기억하기_가져오기() throws Exception{
        //given
        String userId = "eBysBT8JaKdDVocYBcFpSlsBsc03";
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
