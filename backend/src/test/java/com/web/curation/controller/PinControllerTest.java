package com.web.curation.controller;

import com.web.curation.config.FirebaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "userId", password = "")
    void 모든_핀_가져오기() throws Exception{
        mockMvc.perform(get("/api/v1/pins"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
