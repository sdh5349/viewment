package com.web.curation.service.image;

import com.web.curation.domain.Image;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.article.ArticleImage;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.repository.article.ArticleImageRepository;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.image.ImageRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * com.web.curation.service.image
 * ImageService.java
 *
 * @author 이주희
 * @date 2021-01-27
 * @변경이력 2021-01-28 프로필 사진 업로드 기능 추가
 **/

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    @Value("${image.path}")
    private String DIR;
    private final ImageRepository imageRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public void saveArtcleImage(MultipartFile multipartFile, Long articleId, int no) throws Exception {
        String path = "article/" + articleId + "-" + no;

        Image image = new Image();
        image.setPath(path);
        Image savedImage = imageRepository.save(image);

        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Article", articleId.toString());
                }
        );
        article.addArticleImage(ArticleImage.createArticleImage(article, savedImage, no));


        File dest = new File(DIR + path);
        multipartFile.transferTo(dest);
    }

    public void saveProfileImage(MultipartFile profileImage, String userId) throws Exception {
        String path = "profile/" + userId;
        Image image = new Image();
        image.setPath(path);
        Image savedImage = imageRepository.save(image);

        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException(userId + "은 등록되지 않은 사용자입니다.");
                });
        user.setProfileImage(savedImage);

        File dest = new File(DIR + path);
        profileImage.transferTo(dest);
    }

    public void updateProfileImage(MultipartFile profileImage, String userId) throws Exception {
        String path = "profile/" + userId;

        File dest = new File(DIR + path);
        if (dest.exists())
            dest.delete();

        profileImage.transferTo(dest);
    }

    public void deleteProfileImage(String userId) {
        String path = "profile/" + userId;

        File dest = new File(DIR + path);
        if (dest.exists())
            dest.delete();

        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UsernameNotFoundException(userId + "은 등록되지 않은 사용자입니다.");
                });
        imageRepository.delete(user.getProfileImage());
    }
}
