package com.web.curation.service.article;

import com.web.curation.domain.Image;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.article.ArticleImage;
import com.web.curation.domain.article.Hashtag;
import com.web.curation.dto.ArticleDto;
import com.web.curation.repository.UserRepository;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.hashtag.HashtagRepository;
import com.web.curation.repository.image.ImageRepository;
import com.web.curation.repository.pin.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final ImageRepository imageRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Article write(ArticleDto articleDto) {
        Article article = new Article();

        //TODO optional 확인
        User user = userRepository.findById(articleDto.getUserId()).get();
        article.setUser(user);


        Pin pin = null;
        if (articleDto.getPinId() != null)
            pin = pinRepository.findById(articleDto.getPinId());
        else {
            Pin newPin = new Pin();
            newPin.setLocation(articleDto.getLat(), articleDto.getLng());
            // TODO kakao api에서 위치의 주소 받아오기
            newPin.setAddress("추후에 수정");
            Long savedPinId = pinRepository.save(newPin);
            pin = pinRepository.findById(savedPinId);
        }
        article.setPin(pin);

        for (int i = 0; i < articleDto.getImageIds().size(); i++) {
            Long imageId = articleDto.getImageIds().get(i);
            //TODO optional 확인
            Image image = imageRepository.findById(imageId).get();
            article.addArticleImage(ArticleImage.createArticleImage(image, i));
        }

        for (String contents : articleDto.getHashtags()) {
            List<Hashtag> hashtags = hashtagRepository.findByContents(contents);
            if (hashtags.size() != 0) {
                article.addHashtag(hashtags.get(0));
            } else {
                Hashtag hashtag = new Hashtag();
                hashtag.setContents(contents);
                Hashtag savedHashtag = hashtagRepository.save(hashtag);
                article.addHashtag(savedHashtag);
            }
        }

        article.setContents(articleDto.getContents());

        articleRepository.save(article);

        return article;
    }

    public Optional<Article> findByArticleId(Long articleId) {
        return articleRepository.findByArticleId(articleId);
    }

    public List<Article> findByHashtag(String hashtag) {
        return articleRepository.findByHashtag(hashtag);
    }

    public Long modify(ArticleDto articleDto) {
        return 0L;
    }

}
