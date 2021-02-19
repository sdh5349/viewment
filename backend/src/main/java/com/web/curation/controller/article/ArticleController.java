package com.web.curation.controller.article;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.curation.commons.PageRequest;
import com.web.curation.domain.article.Article;
import com.web.curation.dto.article.ArticleDto;
import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.dto.article.ArticleInfoDto;
import com.web.curation.dto.article.ArticleSimpleDto;
import com.web.curation.dto.user.SimpleUserInfoDto;
import com.web.curation.service.article.ArticleService;
import com.web.curation.service.image.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.controller.article
 * ArticleController.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력 김종성: 좋아요 기능 추가
 * @변경이력 이주희: 기본 피드 게시글 조회 기능 추가
 * 이주희 21-02-09 기본 피드 게시글 조회 기능 수정
 **/


@Api(tags = {"Articles"})
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ImageService imageService;

    @ApiOperation(value = "게시글 작성, 핀X", notes = "adressName, contents, hashtags, lat, lng, userId 필수 \n 해시태그가 없을 경우 비어있는 배열이라도 보내주세요")
    @PostMapping({""})
    public ResponseEntity<Long> writeArticleNoPin(@RequestBody ArticleDto articleDto) {

        Article savedArticle = null;
        try {
            savedArticle = articleService.write(articleDto);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
        return ResponseEntity.ok().body(savedArticle.getArticleId());
    }

    @ApiOperation(value = "게시글 작성, 핀O", notes = "adressName, contents, hashtags, pinId, userId 필수 \n 해시태그가 없을 경우 비어있는 배열이라도 보내주세요")
    @PostMapping({"/pin"})
    public ResponseEntity<Long> writeArticlewithPin(@RequestBody ArticleDto articleDto) {

        Article savedArticle = null;
        try {
            savedArticle = articleService.write(articleDto);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }
        return ResponseEntity.ok().body(savedArticle.getArticleId());
    }

    @ApiOperation(value = "article id로 게시글 조회, 상세페이지에 사용")
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleInfoDto> getArticleByArticleId(@PathVariable("articleId") Long articleId, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();

        ArticleInfoDto articleInfoDto = articleService.findByArticleId(currentUserId, articleId);
        return ResponseEntity.ok().body(articleInfoDto);
    }

    @ApiOperation(value = "user id로 게시글 조회, 사용자 프로필 피드 탭에 사용")
    @GetMapping("/searchbyuserid/{userId}")
    public ResponseEntity<List<ArticleSimpleDto>> getArticleByUserId(@PathVariable("userId") String userId){
        List<ArticleSimpleDto> articlesimpleDtos = articleService.findByUserId(userId);
        return ResponseEntity.ok().body(articlesimpleDtos);
    }

    @ApiOperation(value = "user id로 게시글 조회, 사용자 프로필 피드 탭에 사용 - 페이징")
    @GetMapping("/searchbyuserid/{userId}/pg")
    public ResponseEntity<Page<ArticleSimpleDto>> getArticleByUserId(@PathVariable("userId") String userId, PageRequest pageRequest){
        Page<ArticleSimpleDto> articlesimpleDtos = articleService.findByUserId(userId, pageRequest.of(Sort.by("wdate").descending()));
        return ResponseEntity.ok().body(articlesimpleDtos);
    }

    @ApiOperation(value = "해시태그 키워드로 게시글 조회, 검색에 사용")
    @GetMapping("/searchbyhashtag/{hashtag}")
    public ResponseEntity<List<ArticleSimpleDto>> getArticlesByHashtag(@PathVariable("hashtag") String hashtag) {
        List<ArticleSimpleDto> articlesimpleDtos = articleService.findByHashtag(hashtag);
        return ResponseEntity.ok().body(articlesimpleDtos);
    }

    @ApiOperation(value = "해시태그 키워드로 게시글 조회, 검색에 사용 - 페이징")
    @GetMapping("/searchbyhashtag/{hashtag}/pg")
    public ResponseEntity<Page<ArticleSimpleDto>> getArticlesByHashtag(@PathVariable("hashtag") String hashtag, PageRequest pageRequest) {
        Page<ArticleSimpleDto> articlesimpleDtos = articleService.findByHashtag(hashtag, pageRequest);
        return ResponseEntity.ok().body(articlesimpleDtos);
    }

    @ApiOperation(value = "뉴스 피드 피드탭 게시글 조회")
    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<ArticleFeedDto>> getArticlesForFeed(@PathVariable("userId") String userId, @RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        List<ArticleFeedDto> articleSimpleDtos = articleService.getArticlesForFeed(userId, lat, lng);
        return ResponseEntity.ok().body(articleSimpleDtos);
    }

    @ApiOperation(value = "뉴스 피드 피드탭 게시글 조회 - 페이징")
    @GetMapping("/feed/{userId}/pg")
    public ResponseEntity<Page<ArticleFeedDto>> getArticlesForFeed(@PathVariable("userId") String userId, @RequestParam("lat") double lat, @RequestParam("lng") double lng, PageRequest pageRequest) {
        Page<ArticleFeedDto> articleSimpleDtos = articleService.getArticlesForFeed(userId, lat, lng, pageRequest.of());
        return ResponseEntity.ok().body(articleSimpleDtos);
    }

    @ApiOperation(value = "pin id들로 게시글 조회 - 모아보기 페이지 => 수정버전!", notes = "start 혹은 end가 공백이거나 null일 경우 전체 조회, 반대의 경우 기간 조회를 합니다")
    @GetMapping("/pins")
    public ResponseEntity<List<ArticleSimpleDto>> getArticlesByPins(@RequestParam("pinId") Long[] pinIds, String start, String end) {
        List<ArticleSimpleDto> articleSimpleDtos = null;
        if(start == null || start.length() == 0 || end == null || end.length() == 0 )
             articleSimpleDtos = articleService.getArticlesByPins(pinIds);
        else
             articleSimpleDtos = articleService.getArticlesByPins(pinIds, start, end);
        return ResponseEntity.ok().body(articleSimpleDtos);
    }

    @ApiOperation(value = "pin id들로 게시글 조회 - 모아보기 페이지 => 이전버전")
    @GetMapping("/pins/period")
    public ResponseEntity<List<ArticleSimpleDto>> getArticlesByPinsPeriod(@RequestParam("pinId") Long[] pinIds, @RequestParam("start") String start, @RequestParam("end") String end) {
        List<ArticleSimpleDto> articleSimpleDtos = articleService.getArticlesByPins(pinIds, start, end);
        return ResponseEntity.ok().body(articleSimpleDtos);
    }

    @ApiOperation(value = "게시글 수정", notes = "contents, hashtags만 수정 가능 \n articleId, contents, hashtags, userId, pinId 필수")
    @PutMapping("/{articleId}")
    public ResponseEntity<?> updateArticle(@PathVariable("articleId") Long articleId, @RequestBody ArticleDto articleDto) {
        articleService.modify(articleDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{articleId}")
    public ResponseEntity<?> deleteArticle(@PathVariable("articleId") Long articleId){
        articleService.delete(articleId);
        return ResponseEntity.ok().build();
    }

    /***
     * 게시글 좋아요
     */
    @ApiOperation(value = "게시글 좋아요")
    @PostMapping("/{articleId}/like")
    public ResponseEntity<String> likeArticle(@PathVariable("articleId") Long articleId, Authentication authentication) {
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();

        try {
            articleService.like(currentUserId, articleId);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().body("like");
    }

    @ApiOperation(value = "게시글 좋아요 취소")
    @DeleteMapping("/{articleId}/unlike")
    public ResponseEntity<String> unlikeArticle(@PathVariable("articleId") Long articleId, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();

        articleService.unlike(currentUserId, articleId);

        return ResponseEntity.ok().body("success");
    }

    @ApiOperation(value = "게시글 좋아요 누른 회원")
    @GetMapping("/{articleId}/like-users")
    public ResponseEntity<Page<SimpleUserInfoDto>> getLikeUser(@PathVariable("articleId") Long articleId, PageRequest pageable, Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        Page<SimpleUserInfoDto> result = articleService.findLikeUsers(currentUserId, articleId, pageable.of());
        return ResponseEntity.ok().body(result);
    }

}
