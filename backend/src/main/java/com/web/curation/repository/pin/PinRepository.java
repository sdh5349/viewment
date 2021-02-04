package com.web.curation.repository.pin;

import com.web.curation.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
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
 * 21-02-03 메인 지도에 뿌릴 핀 가져오기 기능 추가
 *
 *
 **/

@Repository
public interface PinRepository extends JpaRepository<Pin, Long> {

    @Query("SELECT p FROM Article a JOIN a.pin p WHERE a.user = (SELECT u FROM User u WHERE u.id = :userId)")
    List<Pin> findByUserId(String userId);
}
