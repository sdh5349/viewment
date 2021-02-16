package com.web.curation.service.memory;

import com.web.curation.commons.PageRequest;
import com.web.curation.domain.Memory;
import com.web.curation.dto.memory.MemoryDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MemoryService {

    Long createMemory(String userId, MemoryDto memoryDto);

    List<MemoryDto> getMemories(String userId);

    Page<MemoryDto> getMemories(String userId, PageRequest pageRequest);

    Long deleteMemory(Long memoryId);

    Long updateMemory(MemoryDto memoryDto);
}
