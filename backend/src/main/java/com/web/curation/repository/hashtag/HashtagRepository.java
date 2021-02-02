package com.web.curation.repository.hashtag;

import com.web.curation.domain.hashtag.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * com.web.curation.repository.hashtag
 * HashtagRepository.java
 * @date    2021-01-26 오후 3:02
 * @author  이주희
 *
 * @변경이력
 **/

//@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    List<Hashtag> findByContents(String contents);

    List<Hashtag> findByContentsStartingWithIgnoreCase(String contents);

}
