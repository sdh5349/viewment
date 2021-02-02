package com.web.curation.dto.reply;

import com.web.curation.domain.reply.Rereply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RereplyDto {

    private Long rereplyId;
    private String userId;
    private String contents;
    private String wdate;
    private Long parentId;

    public RereplyDto(Rereply rereply) {
        rereplyId = rereply.getRereplyId();
        userId = rereply.getUser().getId();
        contents = rereply.getContents();
        wdate = rereply.getWdate().toString();
    }
}
