package com.web.curation.dto.notification;

import com.web.curation.domain.notification.Noti;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.dto.memory.MemoryDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotiListDto {

    private Long notiId;
    private SimpleUserInfoDto from;
    private SimpleUserInfoDto to;
    private ArticleSimpleDto article;
    private MemoryDto memory;
    private String wdate;
    private String type;

    public NotiListDto(Noti noti) {
        this.notiId = noti.getId();
        this.to = new SimpleUserInfoDto(noti.getFrom());
        this.type = noti.getNotiType().toString();
        this.wdate = noti.getWdate().toString();
        if(type.equals("MEMORY")){
            this.article = new ArticleSimpleDto(noti.getArticle());
            this.memory = new MemoryDto(noti.getMemory());
        } else if (type.equals("FOLLOW")){
            this.from = new SimpleUserInfoDto(noti.getFrom());
        } else {
            this.article = new ArticleSimpleDto(noti.getArticle());
            this.from = new SimpleUserInfoDto(noti.getFrom());
        }
    }
}
