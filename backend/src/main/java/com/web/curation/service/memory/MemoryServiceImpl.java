package com.web.curation.service.memory;

import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import com.web.curation.dto.memory.MemoryDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.exceptions.UserNotFoundException;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.repository.pin.PinRepository;
import com.web.curation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoryServiceImpl implements MemoryService{

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    public Memory getMemory(Long memoryId){
        return memoryRepository.findById(memoryId).orElseThrow(
                ()->{ throw new ElementNotFoundException("Memory", memoryId.toString());}
        );
    }

    @Override
    public Long createWithPin(String userId, MemoryDto memoryDto, Long pinId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> { throw new UserNotFoundException(userId);}
        );

        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> { throw new ElementNotFoundException("Pin", pinId.toString());}
        );

        Memory memory = new Memory();
        memory.setUser(user);
        memory.setPin(pin);
        memoryRepository.save(memory);
        user.addMemory(memory);

        return memory.getId();
    }

    @Override
    public Long createWithoutPin(String userId, MemoryDto memoryDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> { throw new UserNotFoundException(userId);}
        );

        Pin pin = new Pin();
        pin.setAddress("추후에 수정");
        pin.setLocation(memoryDto.getLat(), memoryDto.getLng());
        pinRepository.save(pin);

        Memory memory = new Memory();
        memory.setName(memoryDto.getName());
        memory.setUser(user);
        memory.setPin(pin);
        memory.setRadius(memoryDto.getRadius());

        memoryRepository.save(memory);
        pin.getMemories().add(memory);
        user.addMemory(memory);

        return memory.getId();
    }

    @Override
    public List<MemoryDto> getMemories(String userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException(userId);
        });

        List<MemoryDto> result = memoryRepository.findByUser(findUser).stream()
                .map(memory -> {
                    return new MemoryDto(memory);
                })
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Long deleteMemory(Long memoryId) {
        Memory memory = getMemory(memoryId);
        memoryRepository.delete(memory);
        return memoryId;
    }

    @Override
    public Long updateMemory(MemoryDto memoryDto) {
        Memory memory = getMemory(memoryDto.getMemoryId());
        memory.setRadius(memoryDto.getRadius());
        memory.setName(memoryDto.getName());
        memoryRepository.save(memory);
        return memory.getId();
    }
}
