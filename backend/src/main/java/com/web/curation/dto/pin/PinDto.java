package com.web.curation.dto.pin;

import com.web.curation.domain.Pin;
import com.web.curation.dto.image.ImageDto;
import lombok.Getter;
import lombok.Setter;

/**
 * com.web.curation.dto.pin
 * PinDto.java
 *
 * @author 이주희
 * @date 2021-01-27
 * @변경이력 21-02-04 썸네일 이미지 추가
 **/

@Getter
@Setter
public class PinDto {
    private Long pinId;
    private double lat;
    private double lng;
    private String addressName;
    private ImageDto thumbnail;

    public PinDto(Pin pin) {
        pinId = pin.getPinId();
        addressName = pin.getAddress();
        lat = pin.getLocation().getY();
        lng = pin.getLocation().getX();
        if (pin.getArticles().size() > 0 && pin.getArticles().get(0).getArticleImages().size() > 0)
            thumbnail = new ImageDto(pin.getArticles().get(0).getArticleImages().get(0).getImage());
    }
}
