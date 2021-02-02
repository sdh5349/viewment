package com.web.curation.service.reply;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.reply.Reply;
import com.web.curation.domain.reply.Rereply;
import com.web.curation.dto.reply.ReplyDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.reply.ReplyRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * com.web.curation.service.reply
 * ReplyService.java
 *
 * @author 이주희
 * @date 2021-02-02 오후 3:22
 * @변경이력
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public Reply writeReply(ReplyDto replyDto) {
        Reply reply = new Reply();

        User findUser = userRepository.findById(replyDto.getUserId()).orElseThrow(() -> {
            throw new UserNotFoundException("사용자가 존재하지 않습니다.");
        });
        reply.setUser(findUser);

        Article findArticle = articleRepository.findById(replyDto.getArticleId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", replyDto.getArticleId().toString());
                }
        );
        reply.setArticle(findArticle);

        reply.setContents(replyDto.getContents());

        Reply savedReply = replyRepository.save(reply);
        return savedReply;
    }

    public void updateReply(ReplyDto replyDto) {
        Reply reply = replyRepository.findById(replyDto.getReplyId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", replyDto.getReplyId().toString());
                }
        );
        reply.setContents(replyDto.getContents());
    }

    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", replyId.toString());
                }
        );
        reply.resetArticle();
        replyRepository.delete(reply);
    }

}
