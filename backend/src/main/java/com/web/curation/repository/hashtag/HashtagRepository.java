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

//    @PersistenceContext
//    private EntityManager em;
//
//    public Hashtag save(Hashtag hashtag){
//        em.persist(hashtag);
//        return hashtag;
//    }
//
//    //TODO unique column이라서 어차피 하나인데... 리스트로만 받아올 수 있으려나?
//    public List<Hashtag> findByContents(String contents) {
//        return em.createQuery("select h from Hashtag h where h.contents = :contents", Hashtag.class)
//                .setParameter("contents", contents)
//                .getResultList();
//    }

}
