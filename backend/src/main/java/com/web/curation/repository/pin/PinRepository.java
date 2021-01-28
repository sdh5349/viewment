package com.web.curation.repository.pin;

import com.web.curation.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * com.web.curation.repository.pin
 * PinRepository.java
 * @date    2021-01-26 오후 2:23
 * @author  이주희
 * @modifier  김종성
 *
 * @변경이력
 *
 * Jpa Repository 인터페이스로 변경   2021-01-28 오후 9:58
 *
 *
 *
 **/

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {

}
