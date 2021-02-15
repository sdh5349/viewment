package com.web.curation.repository.notification;

import com.web.curation.domain.User;
import com.web.curation.domain.notification.Noti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Noti, Long> {
    Long countByToAndChecked(User to, boolean checked);
    List<Noti> findByToAndChecked(User to, boolean checked);
}
