package com.web.curation.service.pin;

import com.web.curation.domain.Pin;
import com.web.curation.dto.PinDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.repository.pin.PinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class PinServiceImpl implements PinService{

    private final PinRepository pinRepository;

    public Pin findPin(Long pinId){
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                ()->{throw new ElementNotFoundException("Pin", pinId.toString());}
        );
        return pin;
    }

    @Override
    public PinDto getPin(Long pinId) {
        return new PinDto(findPin(pinId));
    }

    @Override
    public List<PinDto> getPinsAll() {
        List<PinDto> pins = pinRepository.findAll().stream()
                .map(pin -> {
                    return new PinDto(pin);
                })
                .collect(Collectors.toList());
        return pins;
    }

    @Override
    public void deletePin(Long pinId) {
        Pin pin = findPin(pinId);
        pinRepository.delete(pin);
    }

    @Override
    public Long updatePin(PinDto pinDto) {
        return null;
    }
}
