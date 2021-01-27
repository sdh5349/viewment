package com.web.curation.dto;

import com.web.curation.domain.Image;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ArticleDto {

    private Long articleId;
    private String userId;
    private double lat;
    private double lng;
    private Long pinId;
    private String contents;
    private List<String> hashtags;
    private List<MultipartFile> images;
    private List<Long> imageIds;

}
