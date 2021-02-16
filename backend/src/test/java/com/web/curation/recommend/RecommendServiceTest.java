package com.web.curation.recommend;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.recommend.RecommendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class RecommendServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    PinRepository pinRepository;

    @Autowired
    RecommendService recommendService;

    @BeforeEach
    void setUp(){

        for(int i = 1; i<=5; i++){
            User user = new User();
            user.setId(i+"");
            user.setEmail(i+"@");
            user.setNickname(i+"User");

            userRepository.save(user);
        }

    }

    @Test
    void 좋아요_있는_사용자에게_추천() throws Exception{
        //given
        Pin pin = new Pin();
        pin.setLocation(0.111, 1.111);
        pin.setAddress("aaaa");

        pinRepository.save(pin);

        List<Article> articles = new ArrayList<>();
        for(int i = 0; i<3; i++){
            ArticleDto articleDto = setArticleData("4", pin.getPinId(), "content" + i, "tag1", "tag2");
            Article article = articleService.write(articleDto);
            articles.add(article);
            System.out.println(article.getArticleId());
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
        articleService.like("5", articles.get(5).getArticleId());
        articleService.like("5", articles.get(6).getArticleId());

        //when

        List<ArticleFeedDto> result = recommendService.recommendArticle("4", PageRequest.of(0, 10)).getContent();

        //then
        System.out.println();
        result.forEach(articleInfoDto -> System.out.println(articleInfoDto.getArticleId()));
    }

    @Test
    void 좋아요_없는_사용자에게_추천() throws Exception{
        //given
        Pin pin = new Pin();
        pin.setLocation(0.111, 1.111);
        pin.setAddress("aaaa");

        pinRepository.save(pin);

        ArticleDto articleDto = setArticleData("2", pin.getPinId(), "content", "tag1", "tag2");
        Article article = articleService.write(articleDto);


        //when

        List<ArticleFeedDto> result = recommendService.recommendArticle("1", PageRequest.of(0, 10)).getContent();

        //then
        result.forEach(articleInfoDto -> System.out.println(articleInfoDto.getArticleId()));
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
