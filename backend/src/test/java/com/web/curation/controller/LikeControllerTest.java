package com.web.curation.controller;


import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.user.AccountDto;
import com.web.curation.repository.like.LikeRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.user.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

import java.util.ArrayList;
import java.util.List;


/**
 * com.web.curation.controller
 * LikeControllerTest.java
 * @date    2021-02-03 오후 10:35
 * @author  김종성
 *
 * @변경이력
 **/

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ArticleService articleService;

    @Autowired
    AccountService accountService;

    @Autowired
    PinRepository pinRepository;

    @BeforeEach
    void setUp(){
        AccountDto accountDto = new AccountDto();
        accountDto.setUserId("aaa");
        accountDto.setEmail("asd@asd.com");
        accountDto.setNickname("gg");
        accountService.join(accountDto);
    }

    @Test
    @WithMockUser(username = "aaa", password = "")
    void 좋아요() throws Exception{
        //given
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        pinRepository.save(pin);

        ArticleDto articleDto = setArticleData("aaa", pin.getPinId(), "내용1", "tag1", "tag2");
        Article article = articleService.write(articleDto);

        //when
        ResultActions result = mockMvc.perform(post("/api/v1/articles/" +
                +article.getArticleId() + "/like"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username = "aaa", password = "")
    void 좋아요_취소() throws Exception{
        //given
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        pinRepository.save(pin);

        ArticleDto articleDto = setArticleData("aaa", pin.getPinId(), "내용1", "tag1", "tag2");
        Article article = articleService.write(articleDto);

        articleService.like("aaa", article.getArticleId());

        //when
        ResultActions result = mockMvc.perform(delete("/api/v1/articles/" +
                +article.getArticleId() + "/unlike"));

        //then
        result.andExpect(status().isOk()).andDo(print());

        Assertions.assertThat(articleService.findLikeUsers("aaa",
                article.getArticleId(),
                PageRequest.of(0, 10)).getContent().size()).isEqualTo(0);
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
