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
    public void 게시글_작성() {
        ArticleDto articleDto = new ArticleDto();

        articleDto.setUserId("aaa");

        articleDto.setPinId(1L);

        List<Long> imageIds = new ArrayList<>();
        imageIds.add(2L);
        imageIds.add(3L);
        articleDto.setImageIds(imageIds);

        List<String> hashtags = new ArrayList<>();
        hashtags.add("tag1");
        hashtags.add("tag2");
        hashtags.add("tag3");
        articleDto.setHashtags(hashtags);

        articleDto.setContents("내용이다");


        Article savedArticle = articleService.write(articleDto);

        em.flush();

        // TODO optional 체크
        Assertions.assertEquals(articleRepository.findByArticleId(savedArticle.getArticleId()).get(),savedArticle);

    }
}
