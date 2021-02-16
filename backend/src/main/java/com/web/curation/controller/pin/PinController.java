package com.web.curation.controller.pin;

import com.web.curation.dto.pin.PinDto;
import com.web.curation.service.pin.PinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * com.web.curation.controller.pin
 * PinController.java
 *
 * @author 김종성
 * @date 2021-01-27 오전 10:25
 *
 * @변경이력
 * 21-02-03 메인 지도에 뿌릴 핀 가져오기 기능 추가
 **/
@Api(tags = {"4. Pin"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

    @ApiOperation(value = "모든 핀 가져오기")
    @GetMapping("")
    public ResponseEntity<List<PinDto>> getPinsAll() {
        List<PinDto> pins = pinService.getPinsAll();

        return ResponseEntity.ok().body(pins);
    }

    @ApiOperation(value = "핀 가져오기")
    @GetMapping("/{pinId}")
    public ResponseEntity<PinDto> getPin(@PathVariable("pinId") Long pinId) {
        PinDto pin = pinService.getPin(pinId);
        return ResponseEntity.ok().body(pin);
    }

    @ApiOperation(value = "사용자 페이지 지도탭에 노출될 핀 조회 (사용자가 올린 글만 조회)")
    @GetMapping("/forusermap")
    public ResponseEntity<?> getPinsForUserMap(@RequestParam("userId") String userId) {
        List<PinDto> pins = pinService.getPinsForUserMap(userId);
        return ResponseEntity.ok().body(pins);
    }

    @ApiOperation(value = "뉴스피드 지도에 노출될 핀 조회")
    @GetMapping("/formap")
    public ResponseEntity<?> getPinsForMap(@RequestParam("userId") String userId) {
        List<PinDto> pins = pinService.getPinsForMap(userId);
        return ResponseEntity.ok().body(pins);
    }

    @ApiOperation(value = "인기 게시글 추천에 노출될 핀 조회")
    @GetMapping("/fortrend")
    public ResponseEntity<?> getPinsForTrend(@RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        List<PinDto> pins = pinService.getPinsForTrend(lat, lng);
        return ResponseEntity.ok().body(pins);
    }

}
