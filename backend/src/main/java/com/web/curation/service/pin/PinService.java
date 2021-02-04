package com.web.curation.service.pin;


import com.web.curation.dto.pin.PinDto;

import java.util.List;

public interface PinService {

    public PinDto getPin(Long pinId);
    public List<PinDto> getPinsAll();
    public void deletePin(Long pinId);
    public Long updatePin(PinDto pinDto);

}
