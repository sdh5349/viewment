package com.web.curation.dto.reply;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReplyDto {

    private Long replyId;
    private String userId;
    private String contents;
    private String wdate;
    private Long articleId;

}
