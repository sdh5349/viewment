package com.web.curation.dto.notification;

import com.web.curation.domain.Memory;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.dto.image.ImageDto;
import com.web.curation.dto.memory.MemoryDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class FirebaseNotiDto {

    private SimpleUserInfoDto target;
    private SimpleUserInfoDto from;
    private Long articleId;
    private String articleThumbnailPath;
    private String memoryName;
    private String wdate;
    private String type;

    public FirebaseNotiDto(User user, Article article, Memory memory) {
        this.target = new SimpleUserInfoDto(user);
        this.articleId = article.getArticleId();
        this.articleThumbnailPath = "thumbnail/"+articleId;
        this.memoryName = memory.getName();
        long time = System.currentTimeMillis();
        Timestamp t = new Timestamp(time);
        this.wdate = t.toString();
        this.type = "MEMORY";
    }

    public FirebaseNotiDto(User target, User from) {
        this.target = new SimpleUserInfoDto(target);
        this.from = new SimpleUserInfoDto(from);
        long time = System.currentTimeMillis();
        Timestamp t = new Timestamp(time);
        this.wdate = t.toString();
        this.type = "FOLLOW";
    }

    public FirebaseNotiDto(User target, User from, Article article) {
        this.target = new SimpleUserInfoDto(target);
        this.from = new SimpleUserInfoDto(from);
        this.articleId = article.getArticleId();
        this.articleThumbnailPath = "thumbnail/"+articleId;
        long time = System.currentTimeMillis();
        Timestamp t = new Timestamp(time);
        this.wdate = t.toString();
        this.type = "LIKE";
    }
}
