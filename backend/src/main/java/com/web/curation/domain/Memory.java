package com.web.curation.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


/**
 * com.web.curation.domain
 * Memory.java
 * @date    2021-01-22 오후 3:41
 * @author  김종성
 *
 * @변경이력
 **/

@Entity
@Getter
@Setter
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEMORY_ID")
    private Long id;

    @Column(nullable = false, name="MEMORY_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="PIN_ID")
    private Pin pin;

    @ColumnDefault("50")
    private int radius;




}
