package com.web.curation.controller.article;

import com.web.curation.commons.PageRequest;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * com.web.curation.controller.article
 * ArticleController.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력 김종성: 좋아요 기능 추가
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

    /***
     * 게시글 좋아요
     */
    @PostMapping("/{articleId}/like")
    public ResponseEntity<String> likeArticle(@PathVariable("articleId") Long articleId, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();

        articleService.like(currentUserId, articleId);

        return ResponseEntity.ok().body("like");
    }

    @DeleteMapping("/{articleId}/unlike")
    public ResponseEntity<String> unlikeArticle(@PathVariable("articleId") Long articleId, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();

        articleService.unlike(currentUserId, articleId);

        return ResponseEntity.ok().body("success");
    }

    @GetMapping("/{articleId}/likes")
    public ResponseEntity<Page<SimpleUserInfoDto>> getLikeUser(@PathVariable("articleId") Long articleId, PageRequest pageable, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        Page<SimpleUserInfoDto> result = articleService.findLikeUsers(currentUserId, articleId, pageable.of());
        return ResponseEntity.ok().body(result);
    }

}
