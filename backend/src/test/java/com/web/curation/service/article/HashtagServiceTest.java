package com.web.curation.service.article;

import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.service.hashtag.HashtagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * com.web.curation.service.article
 * HashtagServiceTest.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@SpringBootTest
@Transactional
public class HashtagServiceTest {

    @Autowired
    HashtagService hashtagService;


    @PersistenceContext
    EntityManager em;


    @Test
    public void 검색어를_포함하는_해시태그_조회(){
        Hashtag tag1 = new Hashtag();
        tag1.setContents("ssafy123");
        em.persist(tag1);
        Hashtag tag2 = new Hashtag();
        tag2.setContents("SSAhi1234");
        em.persist(tag2);

        List<HashtagDto> result = hashtagService.findByContentsStartingWithIgnoreCase("ssa");

        Assertions.assertEquals(2, result.size());
    }

}
