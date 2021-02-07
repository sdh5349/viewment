package com.web.curation.service.pin;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.connection.Follow;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

@Service
@Transactional
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {

    private final PinRepository pinRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public Pin findPin(Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Pin", pinId.toString());
                }
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
                .filter(pin->pin.getArticles().size() > 0)
                .map(pin -> {
                    return new PinDto(pin);
                })
                .collect(Collectors.toList());
        return pins;
    }

    @Override
    public List<PinDto> getPinsForMap(String userId, boolean includeMine, boolean includeFollowings) {
        Set<Pin> pins = new HashSet<>();
        if (includeMine) {
            pins.addAll(pinRepository.findByUserId(userId));
        }
        if (includeFollowings) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> {
                        throw new UserNotFoundException();
                    }
            );
            List<Follow> followings = followRepository.findByFrom(user);
            for (int i = 0; i < followings.size(); i++) {
                pins.addAll(pinRepository.findByUserId(followings.get(i).getTo().getId()));
            }
        }

        List<PinDto> result = pins.stream()
                .map(pin -> {
                    return new PinDto(pin);
                })
                .collect(Collectors.toList());

        return result;
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
