package com.web.curation.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private String path;

}
