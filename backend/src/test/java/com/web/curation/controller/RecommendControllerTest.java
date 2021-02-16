package com.web.curation.controller;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.user.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RecommendControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    PinRepository pinRepository;

    @Autowired
    FollowService followService;

    @BeforeEach
    void setUp(){
        for(int i = 1; i<=5; i++){
            User user = new User();
            user.setId(i+"");
            user.setEmail(i+"@");
            user.setNickname(i+"User");
            userRepository.save(user);
        }

        Pin pin = new Pin();
        pin.setLocation(0.111, 1.111);
        pin.setAddress("aad aa");
        pinRepository.save(pin);

        List<Article> articles = new ArrayList<>();
        for(int i = 0; i<3; i++){
            ArticleDto articleDto = setArticleData("4", pin.getPinId(), "content" + i, "tag1", "tag2");
            Article article = articleService.write(articleDto);
            articles.add(article);
        }
        for(int i = 3; i<=7; i++){
            ArticleDto articleDto = setArticleData("5", pin.getPinId(), "content" + i, "tag1", "tag2");
            Article article = articleService.write(articleDto);
            articles.add(article);
        }

        articleService.like("1", articles.get(0).getArticleId());
        articleService.like("1", articles.get(1).getArticleId());
        articleService.like("1", articles.get(2).getArticleId());
        articleService.like("1", articles.get(3).getArticleId());

        articleService.like("2", articles.get(0).getArticleId());
        articleService.like("2", articles.get(1).getArticleId());
        articleService.like("2", articles.get(2).getArticleId());
        articleService.like("2", articles.get(4).getArticleId());

        articleService.like("3", articles.get(1).getArticleId());
        articleService.like("3", articles.get(4).getArticleId());
        articleService.like("3", articles.get(5).getArticleId());

        articleService.like("4", articles.get(0).getArticleId());
        articleService.like("4", articles.get(1).getArticleId());
        articleService.like("4", articles.get(3).getArticleId());

        articleService.like("5", articles.get(0).getArticleId());
        articleService.like("5", articles.get(2).getArticleId());
    }

    @Test
    @WithMockUser(username = "1", password = "")
    void 추천_성공() throws Exception{
        //given
        followService.follow("1", "5");
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/recommendations/articles?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
    }

    @Test
    @WithMockUser(username = "1", password = "")
    void 추천_결과가_없음() throws Exception{
        //given

        //when
        MvcResult result = mockMvc.perform(get("/api/v1/recommendations/articles?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
    }

    @Test
    @WithMockUser(username = "1", password = "")
    void 추천_페이징_테스트() throws Exception{
        //given
        followService.follow("1", "5");
        //when
        MvcResult result = mockMvc.perform(get("/api/v1/recommendations/articles?page=1&size=4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
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
