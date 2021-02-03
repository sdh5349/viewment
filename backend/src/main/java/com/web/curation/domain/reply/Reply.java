package com.web.curation.domain.reply;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.domain.reply
 * Reply.java
 * @date    2021-02-02 오전 10:24
 * @author  이주희
 *
 * @변경이력
 **/

@Entity
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Rereply> child = new ArrayList<>();

    @CreationTimestamp
    private Timestamp wdate;

    public void setUser(User user) {
        this.user = user;
        user.getReplies().add(this);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setArticle(Article article) {
        this.article = article;
        article.getReplies().add(this);
    }

    public void resetArticle() {
        article.getReplies().remove(this);
        article = null;
    }
}