package com.web.curation.domain.memory;

import com.web.curation.domain.Memory;
import com.web.curation.domain.Pin;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemoryPin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMORY_PIN_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id")
    Memory memory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    Pin pin;

    public static MemoryPin createMemoryPin(Memory memory, Pin pin) {
        MemoryPin memoryPin = new MemoryPin();
        memoryPin.setMemory(memory);
        memoryPin.setPin(pin);
        return memoryPin;
    }

    public void resetMemoryPin() {
        memory.getNearbyPins().remove(this);
        memory = null;
        pin = null;
    }
}
