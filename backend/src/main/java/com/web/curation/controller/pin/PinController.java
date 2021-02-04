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

    @ApiOperation(value = "지도에 뿌릴 핀 가져오기 - 노출 정도 설정 가능")
    @GetMapping("/formap")
    public ResponseEntity<?> getPinsForMap(@RequestParam("userId") String userId, @RequestParam("mine") boolean includeMine, @RequestParam("follow") boolean includeFollowings) {
        List<PinDto> pins = pinService.getPinsForMap(userId, includeMine, includeFollowings);
        return ResponseEntity.ok().body(pins);
    }

}
