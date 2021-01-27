package com.web.curation.domain.article;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.domain.article
 * Hashtag.java
 * @date    2021-01-25 오전 9:30
 * @author  이주희
 *
 * @변경이력
 **/

@Entity
@Table(name = "hashtag")
@Getter @Setter
public class Hashtag {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long hashtagId;

    @Column(nullable = false, unique=true)
    private String contents;

    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

}
