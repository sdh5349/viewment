package com.web.curation.controller.pin;

import com.web.curation.dto.PinDto;
import com.web.curation.service.pin.PinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pins")
public class PinController {

    private final PinService pinService;

    @GetMapping("")
    public ResponseEntity<?> getPinsAll(){
        List<PinDto> pins = pinService.getPinsAll();

        return ResponseEntity.ok().body(pins);
    }

    @GetMapping("/{pinId}")
    public ResponseEntity<?> getPin(@PathVariable("pinId") Long pinId){
        PinDto pin = pinService.getPin(pinId);
        return ResponseEntity.ok().body(pin);
    }


}
