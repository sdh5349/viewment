package com.web.curation.dto.pin;

import com.web.curation.domain.Pin;
import lombok.Getter;
import lombok.Setter;

/**
 * com.web.curation.dto.pin
 * PinDto.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@Getter
@Setter
public class PinDto {
    private Long pinId;
    private double lat;
    private double lng;
    private String addressName;

    public PinDto(Pin pin) {
        pinId = pin.getPinId();
        addressName = pin.getAddress();
        lat = pin.getLocation().getX();
        lng = pin.getLocation().getY();
    }
}
