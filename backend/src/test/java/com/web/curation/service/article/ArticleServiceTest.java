package com.web.curation.service.article;

import com.web.curation.domain.Image;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.article.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.service.article
 * ArticleServiceTest.java
 *
 * @author 이주희
 * @date 2021-01-26 오후 5:34
 * @변경이력
 **/

@SpringBootTest
@Transactional
public class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;


    @PersistenceContext
    EntityManager em;


    @BeforeEach
    public void init() {
        User user = new User();
        user.setId("aaa");
        user.setEmail("asd@asd.com");
        user.setNickname("gg");

        em.persist(user);

        Hashtag hashtag = new Hashtag();
        hashtag.setContents("tag1");

        em.persist(hashtag);
    }


    @Test
    public void 게시글_작성_pinId_있음() {
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        em.persist(pin);

        ArticleDto articleDto = setArticleData("aaa", pin.getPinId(), "내용", "tag1", "tag2");


        Article savedArticle = articleService.write(articleDto);

        Article findArticle = articleRepository.findById(savedArticle.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleDto.getArticleId().toString());
                }
        );

        Assertions.assertEquals(findArticle, savedArticle);

    }

    @Test
    public void 게시글_작성_pinId_없음() {
        ArticleDto articleDto = setArticleData("aaa", 0L, "내용", "tag1", "tag2");
        articleDto.setLat(36.471285);
        articleDto.setLng(127.082347);

        Article savedArticle = articleService.write(articleDto);

        Article findArticle = articleRepository.findById(savedArticle.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleDto.getArticleId().toString());
                }
        );

        Assertions.assertEquals(findArticle, savedArticle);
    }

    @Test
    public void 게시글_번호로_조회() {
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        em.persist(pin);

        ArticleDto articleDto = setArticleData("aaa", pin.getPinId(), "내용", "tag1", "tag2");

        Article savedArticle = articleService.write(articleDto);

        Article findArticle = articleRepository.findById(savedArticle.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleDto.getArticleId().toString());
                }
        );

        Assertions.assertEquals(savedArticle, findArticle);
        Assertions.assertEquals(articleDto.getContents(), findArticle.getContents());

    }

    @Test
    public void 해시태그로_조회() {
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        em.persist(pin);

        ArticleDto articleDto1 = setArticleData("aaa", pin.getPinId(), "내용1", "tag1", "tag2");
        ArticleDto articleDto2 = setArticleData("aaa", pin.getPinId(), "내용2", "tag1", "tag3");
        ArticleDto articleDto3 = setArticleData("aaa", pin.getPinId(), "내용3", "tag2", "tag3");

        articleService.write(articleDto1);
        articleService.write(articleDto2);
        articleService.write(articleDto3);

        List<Article> findArticles = articleRepository.findByHashtag("tag1");

        for (Article article : findArticles) {
            System.out.println(article.getContents());
        }
        Assertions.assertEquals(findArticles.size(), 2);

    }

    @Test
    public void 게시글_수정() {
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        em.persist(pin);

        ArticleDto articleDto1 = setArticleData("aaa", pin.getPinId(), "내용1", "tag1", "tag2");
        ArticleDto articleDto2 = setArticleData("aaa", pin.getPinId(), "내용2", "tag1", "tag3");
        ArticleDto articleDto3 = setArticleData("aaa", pin.getPinId(), "내용3", "tag2", "tag3");

        Article savedArticle = articleService.write(articleDto1);
        articleService.write(articleDto2);
        articleService.write(articleDto3);

        ArticleDto articleDto = setArticleData("aaa", pin.getPinId(), "내용 수정", "tag5", "tag6", "tag4");
        articleDto.setArticleId(savedArticle.getArticleId());

        articleService.modify(articleDto);

        Article findArticle = articleRepository.findById(savedArticle.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleDto.getArticleId().toString());
                }
        );

        for (Hashtag hash : findArticle.getHashtags())
            System.out.println(hash.getContents());

        Assertions.assertEquals(findArticle.getContents(), "내용 수정");
        Assertions.assertEquals(findArticle.getHashtags().size(), 3);

    }

    @Test
    public void 게시글_삭제() {
        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        em.persist(pin);

        ArticleDto articleDto1 = setArticleData("aaa", pin.getPinId(), "내용1", "tag1", "tag2");
        ArticleDto articleDto2 = setArticleData("aaa", pin.getPinId(), "내용2", "tag1", "tag3");
        ArticleDto articleDto3 = setArticleData("aaa", pin.getPinId(), "내용3", "tag2", "tag3");

        articleService.write(articleDto1);
        Article savedArticle = articleService.write(articleDto2);
        articleService.write(articleDto3);

        int sizeBefore = articleRepository.findByUserId("aaa").size();
        Assertions.assertEquals(sizeBefore, 3);

        articleService.delete(savedArticle.getArticleId());

        List<Article> findArticles = articleRepository.findByUserId("aaa");
        for (Article article : findArticles)
            System.out.println(article.getContents());

        Assertions.assertEquals(findArticles.size(), 2);
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
