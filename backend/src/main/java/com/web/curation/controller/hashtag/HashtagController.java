package com.web.curation.controller.hashtag;

import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.service.hashtag.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * com.web.curation.controller.hashtag
 * HashtagController.java
 * @date    2021-01-28 오후 12:45
 * @author  이주희
 *
 * @변경이력
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hashtags")
public class HashtagController {

    private HashtagService hashtagService;

    @GetMapping("/{keyword}")
    public ResponseEntity<?> getHashtagByKeyword(@PathVariable("keyword") String keyword){
        List<HashtagDto> hashtagDtos = hashtagService.findByContentsStartingWithIgnoreCase(keyword);
        return ResponseEntity.ok().body(hashtagDtos);
    }
}
