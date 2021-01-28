package com.web.curation.domain.article;

import com.web.curation.domain.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * com.web.curation.domain.article
 * ArticleImage.java
 * @date    2021-01-25 오전 9:28
 * @author  이주희
 *
 * @변경이력
 **/

@Entity
@Getter @Setter
public class ArticleImage {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "article_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(nullable = false)
    private byte no;

    public static ArticleImage createArticleImage(Article article, Image image, int no) {
        ArticleImage articleImage = new ArticleImage();
        articleImage.setArticle(article);
        articleImage.setImage(image);
        articleImage.setNo((byte) no);
        return articleImage;
    }
}
