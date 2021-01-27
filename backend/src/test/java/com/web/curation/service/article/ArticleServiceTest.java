package com.web.curation.service.article;

import com.web.curation.domain.Image;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.article.Hashtag;
import com.web.curation.dto.ArticleDto;
import com.web.curation.repository.article.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.service.article
 * ArticleServiceTest.java
 * @date    2021-01-26 오후 5:34
 * @author  이주희
 *
 * @변경이력
 **/

@SpringBootTest
@Transactional
@Rollback(false)
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

        Pin pin = new Pin();
        pin.setLocation(36.471289, 127.082326);
        pin.setAddress("충청남도 공주시");

        em.persist(pin);
        em.persist(pin);

        Image image1 = new Image();
        image1.setPath("/asb");
        Image image2 = new Image();
        image2.setPath("/asbf");
        Image image3 = new Image();
        image3.setPath("/asbf");

        em.persist(image1);
        em.persist(image2);
        em.persist(image3);

        Hashtag hashtag = new Hashtag();
        hashtag.setContents("tag1");

        em.persist(hashtag);

    }


    @Test
    public void 게시글_작성_pinId_있음() {
        ArticleDto articleDto = setArticleData("aaa", 1L, 1L, "내용", "tag1", "tag2");


        Article savedArticle = articleService.write(articleDto);

        em.flush();

        // TODO optional 체크
        Assertions.assertEquals(articleRepository.findByArticleId(savedArticle.getArticleId()).get(),savedArticle);

    }

    @Test
    public void 게시글_작성_pinId_없음() {
        ArticleDto articleDto = setArticleData("aaa", 0L, 1L, "내용", "tag1", "tag2");
        articleDto.setLat(36.471285);
        articleDto.setLng( 127.082347);

        Article savedArticle = articleService.write(articleDto);

        em.flush();

        // TODO optional 체크
        Assertions.assertEquals(articleRepository.findByArticleId(savedArticle.getArticleId()).get(),savedArticle);

    }

    @Test
    public void 게시글_번호로_조회() {
        ArticleDto articleDto = setArticleData("aaa", 1L, 1L, "내용", "tag1", "tag2");

        Article savedArticle = articleService.write(articleDto);

        Article findArticle =  articleRepository.findByArticleId(savedArticle.getArticleId()).get();

        Assertions.assertEquals(savedArticle, findArticle);
        Assertions.assertEquals(articleDto.getContents(), findArticle.getContents());

    }

    @Test
    public void 해시태그로_조회() {
        ArticleDto articleDto1 = setArticleData("aaa", 1L, 1L, "내용1", "tag1", "tag2");
        ArticleDto articleDto2 = setArticleData("aaa", 1L, 2L, "내용2", "tag1", "tag3");
        ArticleDto articleDto3 = setArticleData("aaa", 1L, 3L, "내용3", "tag2", "tag3");

        articleService.write(articleDto1);
        articleService.write(articleDto2);
        articleService.write(articleDto3);

        List<Article> findArticles = articleRepository.findByHashtag("tag1");

        for (Article article:findArticles) {
            System.out.println(article.getContents());
        }
        Assertions.assertEquals(findArticles.size(), 2);

    }

    @Test
    public void 게시글_수정() {
        ArticleDto articleDto1 = setArticleData("aaa", 1L, 1L, "내용1", "tag1", "tag2");
        ArticleDto articleDto2 = setArticleData("aaa", 1L, 2L, "내용2", "tag1", "tag3");
        ArticleDto articleDto3 = setArticleData("aaa", 1L, 3L, "내용3", "tag2", "tag3");

        articleService.write(articleDto1);
        articleService.write(articleDto2);
        articleService.write(articleDto3);

        ArticleDto articleDto = setArticleData("aaa", 1L, 0L, "내용 수정", "tag5", "tag6", "tag4");
        articleDto.setArticleId(1L);

        articleService.modify(articleDto);

        Article findArticle =  articleRepository.findByArticleId(1L).get();

        for (Hashtag hash:findArticle.getHashtags())
            System.out.println(hash.getContents());

        Assertions.assertEquals(findArticle.getContents(), "내용 수정");
        Assertions.assertEquals(findArticle.getHashtags().size(), 3);

    }

    @Test
    public void 게시글_삭제() {
        ArticleDto articleDto1 = setArticleData("aaa", 1L, 1L, "내용1", "tag1", "tag2");
        ArticleDto articleDto2 = setArticleData("aaa", 1L, 2L, "내용2", "tag1", "tag3");
        ArticleDto articleDto3 = setArticleData("aaa", 1L, 3L, "내용3", "tag2", "tag3");

        articleService.write(articleDto1);
        articleService.write(articleDto2);
        articleService.write(articleDto3);

        int sizeBefore = articleRepository.findByUserId("aaa").size();
        Assertions.assertEquals(sizeBefore, 3);

        articleService.delete(2L);

        List<Article> findArticles = articleRepository.findByUserId("aaa");
        for(Article article:findArticles)
            System.out.println(article.getContents());

        Assertions.assertEquals(findArticles.size(), 2);
    }

    public ArticleDto setArticleData(String userId, Long pinId, Long imageId, String contents, String... hashtag) {
        ArticleDto articleDto = new ArticleDto();

        articleDto.setUserId(userId);

        articleDto.setPinId(pinId);

        List<Long> imageIds = new ArrayList<>();
        imageIds.add(imageId);
        articleDto.setImageIds(imageIds);

        List<String> hashtags = new ArrayList<>();
        for (String h:hashtag) {
            hashtags.add(h);
        }
        articleDto.setHashtags(hashtags);

        articleDto.setContents(contents);

        return articleDto;
    }
}
