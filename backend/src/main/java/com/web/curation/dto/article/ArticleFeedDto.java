package com.web.curation.dto.article;

import com.web.curation.domain.article.Article;
import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.image.ImageDto;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * com.web.curation.dto.article
 * ArticleFeedDto.java
 * @date    2021-02-15 오전 7:35
 * @author  이주희
 *
 * @변경이력
 **/

@Getter
@Setter
public class ArticleFeedDto {
    private Long articleId;
    private SimpleUserInfoDto user;
    private PinDto pin;
    private ImageDto thumbnail;
    private String date;
    private String wdate;
    private List<Hashtag> hashtags;
    private double distance;
    private int likes;
    private boolean liked;
    private boolean recommended;

    public ArticleFeedDto(Article article){
        this.articleId = article.getArticleId();
        this.user = new SimpleUserInfoDto(article.getUser());
        this.pin = new PinDto(article.getPin(), article.getPin().getTrendArticleId());
        if(article.getArticleImages().size()>0) {
            this.thumbnail = new ImageDto();
            this.thumbnail.setPath("thumbnail/"+articleId);
        }
        this.date = article.getDate();
        this.wdate = article.getWdate().toString();
    }
}
