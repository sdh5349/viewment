package com.web.curation.domain.article;

import com.web.curation.domain.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    public static ArticleImage createArticleImage(Image image, int no) {
        ArticleImage articleImage = new ArticleImage();
        articleImage.setImage(image);
        articleImage.setNo((byte) no);
        return articleImage;
    }
}
