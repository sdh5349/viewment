package com.web.curation.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * com.web.curation.domain
 * Image.java
 * @date    2021-01-22 오후 3:42
 * @author  김종성
 *
 * @변경이력
 **/

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IMAGE_ID")
    private Long id;

    private String path;

}
