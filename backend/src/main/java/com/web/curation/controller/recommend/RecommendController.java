package com.web.curation.controller.recommend;

import com.web.curation.dto.article.ArticleFeedDto;
import com.web.curation.service.recommend.RecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Recommend"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RecommendController {
    private final RecommendService recommendService;

    @ApiOperation(value = "게시글 추천")
    @GetMapping("/recommendations/articles")
    public ResponseEntity<List<ArticleFeedDto>> recommendArticle(Authentication authentication){
        final String currentUserId = ((UserDetails)authentication.getPrincipal()).getUsername();
        List<ArticleFeedDto> results = recommendService.recommendArticle(currentUserId);
        return ResponseEntity.ok().body(results);
    }

}
