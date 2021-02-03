package com.web.curation.service.reply;

import com.web.curation.domain.User;
import com.web.curation.domain.reply.Reply;
import com.web.curation.domain.reply.Rereply;
import com.web.curation.dto.reply.RereplyDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.reply.ReplyRepository;
import com.web.curation.repository.reply.RereplyRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * com.web.curation.service.reply
 * RereplyService.java
 * @date    2021-02-02 오후 7:08
 * @author  이주희
 *
 * @변경이력
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class RereplyService {

    private final RereplyRepository rereplyRepository;

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;

    public Rereply writeRereply(RereplyDto rereplyDto) {
        Rereply rereply = new Rereply();

        User findUser = userRepository.findById(rereplyDto.getUserId()).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
        rereply.setUser(findUser);

        Reply parent = replyRepository.findById(rereplyDto.getParentId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Reply", rereplyDto.getParentId().toString());
                }
        );
        rereply.setParent(parent);

        rereply.setContents(rereplyDto.getContents());

        Rereply savedRereply = rereplyRepository.save(rereply);
        return savedRereply;
    }

    public void updateRereply(RereplyDto rereplyDto) {
        Rereply rereply = rereplyRepository.findById(rereplyDto.getRereplyId()).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Rereply", rereplyDto.getRereplyId().toString());
                }
        );
        rereply.setContents(rereplyDto.getContents());
    }

    public void deleteRereply(Long replyId) {
        Rereply rereply = rereplyRepository.findById(replyId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Rereply", replyId.toString());
                }
        );
        rereply.resetParent();
        rereplyRepository.delete(rereply);
    }
}
