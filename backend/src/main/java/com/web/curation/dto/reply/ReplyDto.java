package com.web.curation.dto.reply;

import com.web.curation.domain.reply.Reply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ReplyDto {

    private Long replyId;
    private String userId;
    private String contents;
    private String wdate;
    private Long articleId;
    private List<RereplyDto> child;

    public ReplyDto(Reply reply) {
        replyId = reply.getReplyId();
        userId = reply.getUser().getId();
        contents = reply.getContents();
        wdate = reply.getWdate().toString();
        child = new ArrayList<>();
        for (int i = 0; i < reply.getChild().size(); i++) {
            child.add(new RereplyDto(reply.getChild().get(i)));
        }
    }

}
