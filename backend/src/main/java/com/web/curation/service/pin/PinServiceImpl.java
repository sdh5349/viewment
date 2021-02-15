package com.web.curation.service.pin;

import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.dto.pin.PinDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.article.ArticleRepository;
import com.web.curation.repository.follow.FollowRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
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
    private final ArticleRepository articleRepository;
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
                .map(pin -> {
                    return new PinDto(pin, pin.getTrendArticleId());
                })
                .collect(Collectors.toList());
        return pins;
    }

    @Override
    public List<PinDto> getPinsForUserMap(String userId) {
        Set<Pin> pins = new HashSet<>();

        getUser(userId).getArticles().stream()
                .forEach(article -> {
                    pins.add(article.getPin());
                });

        List<PinDto> result = pins.stream()
                .map(pin -> {
                    return new PinDto(pin, pin.getTrendArticleId());
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<PinDto> getPinsForMap(String userId) {
        Set<Pin> pins = new HashSet<>();

        getUser(userId).getMemories().stream()
                .forEach(memory -> {
                    pins.addAll(memory.getNearbyPins());
                });

        List<PinDto> result = pins.stream()
                .map(pin -> {
                    return new PinDto(pin, pin.getTrendArticleId());
                })
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public List<PinDto> getPinsForTrend(double lat, double lng) {

        long time = System.currentTimeMillis()-1000L*60L*60L*24L*7L;
        Timestamp t = new Timestamp(time);

        List<Long[]> pins = pinRepository.findAll().stream()
                .map(pin -> {
                    return new Long[] {pin.getPinId(), articleRepository.countByPinAndWdateAfter(pin, t)};
                })
                .collect(Collectors.toList());

        Collections.sort(pins, new Comparator<Long[]>() {
            @Override
            public int compare(Long[] p1, Long[] p2) {
                Long value = p2[1] -p1[1];
                return value.intValue();
            }
        });

        pins = pins.subList(0, Math.min(pins.size(), 10));

        List<PinDto> result = pins.stream()
                .map(pinId -> {
                    Pin pin = findPin(pinId[0]);
                    return new PinDto(pin, pin.getTrendArticleId());
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

    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
        return user;
    }

}
