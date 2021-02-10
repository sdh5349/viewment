package com.web.curation.dto.article;

import com.web.curation.domain.article.Article;
import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.dto.image.ImageDto;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.dto.reply.ReplyDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.dto.article
 * ArticleSimpleDto.java
 * @date    2021-02-02 오후 11:16
 * @author  이주희
 *
 * @변경이력
 **/

@Getter
@Setter
public class ArticleSimpleDto {
    private Long articleId;
    private SimpleUserInfoDto user;
    private PinDto pin;
    private ImageDto thumbnail;
    private String date;
    private String wdate;
    private double distance;
    private boolean following;

    public ArticleSimpleDto(Article article){
        this.articleId = article.getArticleId();
        this.user = new SimpleUserInfoDto(article.getUser());
        this.pin = new PinDto(article.getPin());
        if(article.getArticleImages().size()>0)
            this.thumbnail = new ImageDto(article.getArticleImages().get(0).getImage());
        this.date = article.getDate();
        this.wdate = article.getWdate().toString();
    }
}
