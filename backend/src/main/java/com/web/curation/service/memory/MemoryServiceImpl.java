package com.web.curation.service.memory;

import com.web.curation.commons.PageRequest;
import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.domain.memory.MemoryPin;
import com.web.curation.dto.memory.MemoryDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import com.web.curation.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * com.web.curation.service.memory
 * MemoryServiceImpl.java
 *
 * @author 김종성
 * @date 2021-01-28
 * @변경이력 이주희 21-02-09 기억하기 주변 핀 저장 기능 추가
 **/

@Service
@RequiredArgsConstructor
@Transactional
public class MemoryServiceImpl implements MemoryService {

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserNotFoundException();
                }
        );
        return user;
    }

    public Memory getMemory(Long memoryId) {
        return memoryRepository.findById(memoryId).orElseThrow(
                () -> {
                    throw new ElementNotFoundException("Memory", memoryId.toString());
                }
        );
    }

    @Override
    public Long createMemory(String userId, MemoryDto memoryDto) {
        User user = getUser(userId);

        Memory memory = new Memory();
        memory.setName(memoryDto.getName());
        memory.setUser(user);
        memory.setLocation(memoryDto.getLat(), memoryDto.getLng());
        memory.setRadius(memoryDto.getRadius());

        memoryRepository.save(memory);
        user.addMemory(memory);
        setNearbyPins(memory, memoryDto);

        return memory.getId();
    }

    @Override
    public List<MemoryDto> getMemories(String userId) {
        User user = getUser(userId);

        List<MemoryDto> result = memoryRepository.findByUser(user).stream()
                .map(memory -> {
                    return new MemoryDto(memory);
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Page<MemoryDto> getMemories(String userId, PageRequest pageRequest) {
        User user = getUser(userId);

        Page<MemoryDto> result = memoryRepository.findByUser(user, pageRequest.of(Sort.by("id").descending()))
                .map(memory -> {
                    return new MemoryDto(memory);
                });
        return result;
    }

    @Override
    public Long deleteMemory(Long memoryId) {
        Memory memory = getMemory(memoryId);
        memory.resetUser();
        memory.resetNearbyPins();
        memoryRepository.delete(memory);
        return memoryId;
    }

    @Override
    public Long updateMemory(MemoryDto memoryDto) {
        Memory memory = getMemory(memoryDto.getMemoryId());
        memory.setName(memoryDto.getName());
        return memory.getId();
    }

    private void setNearbyPins(Memory memory, MemoryDto memoryDto) {
        pinRepository.findAll().stream()
                .forEach(pin -> {
                    if (memoryDto.getRadius() >= DistanceUtil.calcDistance(memoryDto.getLat(), memoryDto.getLng(), pin.getLocation().getY(), pin.getLocation().getX()))
                        memory.addNearbyPins(MemoryPin.createMemoryPin(memory, pin));
                });
    }
}
