package com.web.curation.service.reply;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.reply.Reply;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.reply.ReplyDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.reply.ReplyRepository;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.reply.ReplyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.service
 * ReplyServiceTest.java
 *
 * @author 이주희
 * @date 2021-02-02 오후 4:25
 * @변경이력
 **/

@SpringBootTest
@Transactional
public class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @PersistenceContext
    EntityManager em;

    Long defaultArticleId;

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

        ArticleDto articleDto = new ArticleDto();
        articleDto.setUserId("aaa");
        articleDto.setPinId(pin.getPinId());
        articleDto.setContents("contents");
        Article savedArticle = articleService.write(articleDto);

        defaultArticleId = savedArticle.getArticleId();
    }

    @Test
    public void 댓글_작성() {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setArticleId(defaultArticleId);
        replyDto.setUserId("aaa");
        replyDto.setContents("test contents");

        ReplyDto savedReply = replyService.writeReply(replyDto);
        Reply findReply = replyRepository.findById(savedReply.getReplyId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", savedReply.getReplyId().toString());
                }
        );

        Assertions.assertEquals(savedReply.getArticleId(), findReply.getArticle().getArticleId());
    }

    @Test
    public void 댓글_수정() {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setArticleId(defaultArticleId);
        replyDto.setUserId("aaa");
        replyDto.setContents("test contents");
        ReplyDto savedReply = replyService.writeReply(replyDto);

        ReplyDto updateReplyDto = new ReplyDto();
        updateReplyDto.setReplyId(savedReply.getReplyId());
        updateReplyDto.setContents("update test");

        replyService.updateReply(updateReplyDto);
        Reply updatedReply = replyRepository.findById(savedReply.getReplyId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", savedReply.getReplyId().toString());
                }
        );

        Assertions.assertEquals(savedReply.getArticleId(), updatedReply.getArticle().getArticleId());
    }

    @Test
    public void 댓글_삭제() {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setArticleId(defaultArticleId);
        replyDto.setUserId("aaa");
        replyDto.setContents("test contents");
        ReplyDto savedReply = replyService.writeReply(replyDto);

        Article beforeArticle = articleRepository.findById(defaultArticleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", defaultArticleId.toString());
                }
        );
        Assertions.assertEquals(1, beforeArticle.getReplies().size());

        replyService.deleteReply(savedReply.getReplyId());

        Article afterArticle = articleRepository.findById(defaultArticleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", defaultArticleId.toString());
                }
        );
        Assertions.assertEquals(0, afterArticle.getReplies().size());
    }
}
