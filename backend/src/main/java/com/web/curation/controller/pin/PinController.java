package com.web.curation.controller.pin;

import com.web.curation.dto.pin.PinDto;
import com.web.curation.service.pin.PinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * com.web.curation.controller.pin
 * PinController.java
 * @date    2021-01-27 오전 10:25
 * @author  김종성
 *
 * @변경이력
 **/
@Api(tags = {"4. Pin"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

    @ApiOperation(value = "모든 핀 가져오기")
    @GetMapping("")
    public ResponseEntity<List<PinDto>> getPinsAll(){
        List<PinDto> pins = pinService.getPinsAll();

        return ResponseEntity.ok().body(pins);
    }

    @ApiOperation(value = "핀 가져오기")
    @GetMapping("/{pinId}")
    public ResponseEntity<PinDto> getPin(@PathVariable("pinId") Long pinId){
        PinDto pin = pinService.getPin(pinId);
        return ResponseEntity.ok().body(pin);
    }


}
