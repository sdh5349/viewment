package com.web.curation.repository.pin;

import com.web.curation.domain.Pin;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * com.web.curation.repository.pin
 * PinRepository.java
 * @date    2021-01-26 오후 2:23
 * @author  이주희
 *
 * @변경이력
 **/

@Repository
public class PinRepository {

    @PersistenceContext
    private EntityManager em;

    public Pin findById(Long pinId) {
        return em.find(Pin.class, pinId);
    }

    public Long save(Pin pin) {
        em.persist(pin);
        return pin.getPinId();
    }
}
