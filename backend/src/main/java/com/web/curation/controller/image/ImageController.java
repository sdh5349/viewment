package com.web.curation.controller.image;

import com.web.curation.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
 *
 * @author 이주희
 * @date 2021-01-27
 * @변경이력
 * 2021-01-28 프로필 사진 업로드 기능 추가
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @Value("${image.path}")
    private String DIR;

    @PostMapping("/article/{articleId}")
    public ResponseEntity<?> uploadArticleImages(@PathVariable("articleId") Long articleId, @RequestParam List<MultipartFile> articleImages) throws Exception {
        for (int i = 0; i < articleImages.size(); i++) {
            imageService.saveArtcleImage(articleImages.get(i), articleId, i);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/profile/{userId}")
    public ResponseEntity<?> uploadProfileImage(@PathVariable("userId") String userId, @RequestParam MultipartFile profileImage) throws Exception {
        imageService.saveProfileImage(profileImage, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{dir}/{name}")
    public ResponseEntity<?> getImage(@PathVariable("dir") String dir, @PathVariable("name") String name) throws Exception {
        File image = new File(DIR + dir + "/" + name);
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(image.toPath()));
        byte[] imagearr = FileCopyUtils.copyToByteArray(image);
        return new ResponseEntity<>(imagearr, header, HttpStatus.OK);
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateProfileImage(@PathVariable("userId") String userId, @RequestParam MultipartFile profileImage) throws Exception {
        imageService.updateProfileImage(profileImage, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<?> DeleteProfileImage(@PathVariable("userId") String userId) {
        imageService.deleteProfileImage( userId);
        return ResponseEntity.ok().build();
    }
}
