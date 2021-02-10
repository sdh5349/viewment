package com.web.curation.service.pin;

import com.web.curation.dto.pin.PinDto;

import java.util.List;

/**
 * com.web.curation.repository.pin
 * PinRepository.java
 * @date    2021-01-28 오후 2:23
 * @author  김종성
 * @modifier  이주희
 *
 * @변경이력
 * 21-02-03 메인 지도에 뿌릴 핀 가져오기 기능 추가
 *
 *
 **/

public interface PinService {

    public PinDto getPin(Long pinId);
    public List<PinDto> getPinsAll();
    public void deletePin(Long pinId);
    public Long updatePin(PinDto pinDto);
    List<PinDto> getPinsForUserMap(String userId);
    List<PinDto> getPinsForMap(String userId);
}
