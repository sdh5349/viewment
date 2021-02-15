package com.web.curation.repository.memory;

import com.web.curation.domain.Pin;
import com.web.curation.domain.memory.MemoryPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryPinRepository extends JpaRepository<MemoryPin, Long> {

    List<MemoryPin> findByPin(Pin pin);

}
