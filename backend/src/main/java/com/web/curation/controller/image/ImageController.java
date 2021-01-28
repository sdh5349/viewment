package com.web.curation.controller.image;

import com.web.curation.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * com.web.curation.controller.image
 * ImageController.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;
    private final String DIR = "C:/viewment/image/article/";

    @PostMapping("/{articleId}")
    public ResponseEntity<?> uploadImage(@PathVariable("articleId") Long articleId, @RequestParam List<MultipartFile> articleImages) throws Exception{
        for (int i = 0; i < articleImages.size(); i++) {
            imageService.saveArtcleImage(articleImages.get(i), articleId, i);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{path}")
    public ResponseEntity<?> getImage(@PathVariable("path") String path) throws Exception {
        File image = new File(DIR + path);
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(image.toPath()));
        byte[] imagearr = FileCopyUtils.copyToByteArray(image);
        return new ResponseEntity<>(imagearr, header, HttpStatus.OK);
    }
}
