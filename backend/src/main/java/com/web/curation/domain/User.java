package com.web.curation.domain;

import com.web.curation.domain.connection.Follow;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * com.web.curation.domain
 * User.java
 * @date    2021-01-22 오후 3:42
 * @author  김종성
 *
 * @변경이력
 **/

@Entity
@Getter
@Setter
public class User {
    @Id
    @Column(name="USER_ID", insertable = false, updatable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(name="MESSAGE")
    private String message;

    @Column(name="JOIN_DATE")
    @ColumnDefault("now()")
    private Timestamp joinDate;

    public User(){}
    public User(String id, String email, String nickname){
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    /**
     *
     */

//    @OneToMany
//    @JoinColumn(name="USER_ID")
//    private List<Memory> memories = new ArrayList<>();
//
//
//    /**
//     * Article
//     */
//    @OneToMany
//    @JoinColumn(name="USER_ID")
//    private List<Object> articles = new ArrayList<>();
//
//
//    /**
//     * Reply
//     */
//    @OneToMany
//    @JoinColumn(name="USER_ID")
//    private List<Object> replies = new ArrayList<>();


    //private Image profileImage;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}