package com.web.curation.repository.notification;

import com.web.curation.domain.Memory;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import com.web.curation.domain.notification.Noti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Noti, Long> {
    Long countByToAndChecked(User to, boolean checked);

    List<Noti> findByToAndChecked(User to, boolean checked);

    void deleteByArticle(Article article);
    void deleteByMemory(Memory memory);
    void deleteByFrom(User from);
    void deleteByTo(User to);
}
