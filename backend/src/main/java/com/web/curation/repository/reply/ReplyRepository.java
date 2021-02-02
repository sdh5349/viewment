package com.web.curation.repository.reply;

import com.web.curation.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * com.web.curation.repository.reply
 * ReplyRepository.java
 * @date    2021-02-02 오전 7:08
 * @author  이주희
 *
 * @변경이력
 **/

public interface ReplyRepository extends JpaRepository<Reply, Long> {}
