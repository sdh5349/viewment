package com.web.curation.controller;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.article.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PinRepository pinRepository;

    @Autowired
    ArticleService articleService;

    @BeforeEach
    void setUp(){
        User user = new User();
        user.setId("userId");
        user.setEmail("aaaa");
        user.setNickname("aaaa");

        userRepository.save(user);

    }

    @Test
    @WithMockUser(username = "userId", password = "")
    void 모든_핀_가져오기() throws Exception{
        mockMvc.perform(get("/api/v1/pins"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "userId", password = "")
    void 게시글_있는_핀_가져오기() throws Exception{


        Pin pin  = new Pin();
        pin.setAddress("주소주소");
        pin.setLocation(0.001, 1.021);

        pinRepository.save(pin);

        Pin pin2  = new Pin();
        pin2.setAddress("주소주소22");
        pin2.setLocation(0.0111, 1.021);

        pinRepository.save(pin2);

        ArticleDto articleDto1 = setArticleData("userId", pin.getPinId(), "내용1", "tag1", "tag2");
        Article article = articleService.write(articleDto1);

        mockMvc.perform(get("/api/v1/pins"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public static ArticleDto setArticleData(String userId, Long pinId, String contents, String... hashtag) {
        ArticleDto articleDto = new ArticleDto();

        articleDto.setUserId(userId);

        articleDto.setPinId(pinId);

        List<String> hashtags = new ArrayList<>();
        for (String h : hashtag) {
            hashtags.add(h);
        }
        articleDto.setHashtags(hashtags);

        articleDto.setContents(contents);

        return articleDto;
    }

}
