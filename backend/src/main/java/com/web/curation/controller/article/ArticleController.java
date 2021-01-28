package com.web.curation.controller.article;

import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * com.web.curation.controller.article
 * ArticleController.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ImageService imageService;

    @PostMapping({"", "/pin"})
    public ResponseEntity<?> writeArticleNoPin(@RequestBody ArticleDto articleDto) throws Exception {

        Article savedArticle = articleService.write(articleDto);
        return ResponseEntity.ok().body(savedArticle.getArticleId());
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getArticleByArticleId(@PathVariable("articleId") Long articleId){
        ArticleInfoDto articleInfoDto = articleService.findByArticleId(articleId);
        return ResponseEntity.ok().body(articleInfoDto);
    }

    @GetMapping("/searchbyuserid/{userId}")
    public ResponseEntity<?> getArticleByUserId(@PathVariable("userId") String userId){
        List<ArticleInfoDto> articleInfoDtos = articleService.findByUserId(userId);
        return ResponseEntity.ok().body(articleInfoDtos);
    }

    @GetMapping("/searchbyhashtag/{hashtag}")
    public ResponseEntity<?> getArticlesByHashtag(@PathVariable("hashtag") String hashtag) {
        List<ArticleInfoDto> articleInfoDtos = articleService.findByHashtag(hashtag);
        return ResponseEntity.ok().body(articleInfoDtos);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable("articleId") Long articleId, @RequestBody ArticleDto articleDto) {
        articleService.modify(articleDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable("articleId") Long articleId){
        articleService.delete(articleId);
        return ResponseEntity.ok().build();
    }
}
