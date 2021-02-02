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
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_ID")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="ARTICLE_ID")
    private Article article;
}
