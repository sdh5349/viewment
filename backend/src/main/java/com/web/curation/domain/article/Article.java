package com.web.curation.domain.article;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
public class Article {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ArticleImage> articleImages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "article_hashtag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private List<Hashtag> hashtags = new ArrayList<>();

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @ColumnDefault("now()")
    private LocalDateTime wdate;


    public void setUser(User user) {
        this.user = user;
        user.getArticles().add(this);
    }

    public void setPin(Pin pin) {
        this.pin = pin;
        pin.getArticles().add(this);
    }

    public void setContents(String contents){
        this.contents = contents;
    }

    public void addArticleImage(ArticleImage articleImage){
        articleImages.add(articleImage);
        articleImage.setArticle(this);
    }

    public void addHashtag(Hashtag hashtag){
        hashtags.add(hashtag);
        hashtag.getArticles().add(this);
    }

}
