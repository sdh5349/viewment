package com.web.curation.domain.reply;

import com.web.curation.domain.User;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * com.web.curation.domain.reply
 * Rereply.java
 * @date    2021-02-02 오전 10:24
 * @author  이주희
 *
 * @변경이력
 **/

@Entity
@Getter
public class Rereply  {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "rereply_id")
    private Long rereplyId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @CreationTimestamp
    private Timestamp wdate;

    public void setUser(User user) {
        this.user = user;
//        user.getRereplies().add(this);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }


    public void setParent(Reply parent) {
        this.parent = parent;
        parent.getChild().add(this);
    }

    public void resetParent() {
        parent.getChild().remove(this);
        this.parent = null;
    }
}