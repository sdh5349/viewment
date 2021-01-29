package com.web.curation.dto;

import com.web.curation.domain.Pin;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PinDto {

    private Long pinId;
    private double lng;
    private double lat;
    private String address;

    public PinDto(){};
    public PinDto(Pin pin){
        pinId = pin.getPinId();
        address = pin.getAddress();
        lng = pin.getLocation().getX();
        lat = pin.getLocation().getY();
    }
}
