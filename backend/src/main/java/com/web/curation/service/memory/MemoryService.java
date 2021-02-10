package com.web.curation.service.memory;

import com.web.curation.domain.Memory;
import com.web.curation.dto.memory.MemoryDto;

import java.util.List;
import java.util.Optional;

public interface MemoryService {

    public Long createMemory(String userId, MemoryDto memoryDto);
    public List<MemoryDto> getMemories(String userId);
    public Long deleteMemory(Long memoryId);
    public Long updateMemory(MemoryDto memoryDto);
}
