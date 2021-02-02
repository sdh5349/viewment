package com.web.curation.domain.connection;

import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.web.curation.domain.connection
 * Like.java
 * @date    2021-02-02 오후 3:21
 * @author  김종성
 *
 * @변경이력
 **/

@Entity
@Getter @Setter
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ARTICLE_ID")
    private Article article;
}
