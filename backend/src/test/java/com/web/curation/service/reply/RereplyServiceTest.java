package com.web.curation.service.reply;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.reply.Reply;
import com.web.curation.domain.reply.Rereply;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.reply.ReplyDto;
import com.web.curation.dto.reply.RereplyDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.repository.reply.ReplyRepository;
import com.web.curation.repository.reply.RereplyRepository;
import com.web.curation.service.article.ArticleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * com.web.curation.service.reply
 * RereplyServiceTest.java
 *
 * @author 이주희
 * @date 2021-02-02 오후 7:27
 * @변경이력
 **/

@SpringBootTest
@Transactional
public class RereplyServiceTest {

    @Autowired
    RereplyService rereplyService;

    @Autowired
    RereplyRepository rereplyRepository;

    @Autowired
    ReplyService replyService;

    @Autowired
    ReplyRepository replyRepository;

    @Autowired
    ArticleService articleService;

    @PersistenceContext
    EntityManager em;

    Long defaultParentId;

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

        ReplyDto replyDto = new ReplyDto();
        replyDto.setArticleId(savedArticle.getArticleId());
        replyDto.setUserId("aaa");
        replyDto.setContents("test contents");

        Reply savedReply = replyService.writeReply(replyDto);
        defaultParentId = savedReply.getReplyId();
    }

    @Test
    public void 대댓글_작성() {
        RereplyDto rereplyDto = new RereplyDto();
        rereplyDto.setParentId(defaultParentId);
        rereplyDto.setUserId("aaa");
        rereplyDto.setContents("test contents");

        Rereply savedRereply = rereplyService.writeRereply(rereplyDto);
        Rereply findRereply = rereplyRepository.findById(savedRereply.getRereplyId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", savedRereply.getRereplyId().toString());
                }
        );

        Assertions.assertEquals(savedRereply, findRereply);
    }

    @Test
    public void 대댓글_수정() {
        RereplyDto rereplyDto = new RereplyDto();
        rereplyDto.setParentId(defaultParentId);
        rereplyDto.setUserId("aaa");
        rereplyDto.setContents("test contents");
        Rereply savedRereply = rereplyService.writeRereply(rereplyDto);

        RereplyDto newRereplyDto = new RereplyDto();
        newRereplyDto.setRereplyId(savedRereply.getRereplyId());
        newRereplyDto.setContents("update test");

        rereplyService.updateRereply(newRereplyDto);
        Rereply updatedRereply = rereplyRepository.findById(savedRereply.getRereplyId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Rereply", savedRereply.getRereplyId().toString());
                }
        );

        Assertions.assertEquals(updatedRereply.getContents(), newRereplyDto.getContents());
    }

    @Test
    public void 대댓글_삭제() {
        RereplyDto rereplyDto = new RereplyDto();
        rereplyDto.setParentId(defaultParentId);
        rereplyDto.setUserId("aaa");
        rereplyDto.setContents("test contents");
        Rereply savedRereply = rereplyService.writeRereply(rereplyDto);

        Reply beforeReply = replyRepository.findById(defaultParentId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", defaultParentId.toString());
                }
        );
        Assertions.assertEquals(1, beforeReply.getChild().size());

        rereplyService.deleteRereply(savedRereply.getRereplyId());

        Reply afterReply = replyRepository.findById(defaultParentId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", defaultParentId.toString());
                }
        );
        Assertions.assertEquals(0, afterReply.getChild().size());

    }
}
