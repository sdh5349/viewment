package com.web.curation.domain.article;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtag")
@Getter @Setter
public class Hashtag {

    @Id
    @GeneratedValue
    private Long hashtagId;

    @Column(nullable = false)
    private String contents;

    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY)
    private List<Article> articles = new ArrayList<>();

}
