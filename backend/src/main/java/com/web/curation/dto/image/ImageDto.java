package com.web.curation.dto.image;

import com.web.curation.domain.Image;
import lombok.Getter;
import lombok.Setter;

/**
 * com.web.curation.dto.image
 * ImageDto.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@Getter
@Setter
public class ImageDto {
    private Long id;

    private String path;

    public ImageDto() {}

    public ImageDto(Image image) {
        id = image.getId();
        path = image.getPath();
    }
}
