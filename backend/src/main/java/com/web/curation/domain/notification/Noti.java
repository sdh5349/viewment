package com.web.curation.domain.notification;

import com.web.curation.domain.Memory;
import com.web.curation.domain.User;
import com.web.curation.domain.article.Article;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Noti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NOTI_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="FROM_ID")
    private User from;

    @ManyToOne
    @JoinColumn(name="TO_ID")
    private User to;

    @ManyToOne
    @JoinColumn(name="ARTICLE_ID")
    private Article article;

    @ManyToOne
    @JoinColumn(name="MEMORY_ID")
    private Memory memory;

    private boolean checked;

    @CreationTimestamp
    private Timestamp wdate;

    @Enumerated(EnumType.STRING)
    private NotiType notiType;

}
