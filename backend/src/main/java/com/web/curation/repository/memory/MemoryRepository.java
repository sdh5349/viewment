package com.web.curation.repository.memory;

import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import com.web.curation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    public List<Memory> findByUser(User user);
    Long countByUser(User user);
}
