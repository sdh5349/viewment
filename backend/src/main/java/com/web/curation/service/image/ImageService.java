package com.web.curation.service.image;

import com.web.curation.domain.Image;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.article.ArticleImage;
import com.web.curation.repository.article.ArticleImageRepository;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final String DIR = "C:/viewment/image/article/";
    private final ImageRepository  imageRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;

    public void saveArtcleImage(MultipartFile multipartFile, Long articleId, int no) throws Exception {
        String path = DIR+articleId+"-"+no;

        Image image = new Image();
        image.setPath(path);
        Image savedImage = imageRepository.save(image);

        Article article = articleRepository.findByArticleId(articleId).get();
        article.addArticleImage(ArticleImage.createArticleImage(article, savedImage, no));


        File dest = new File(path);
        multipartFile.transferTo(dest);
    }
}
