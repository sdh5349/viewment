package com.web.curation.repository.image;

import com.web.curation.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * com.web.curation.repository.image
 * ImageRepository.java
 * @date    2021-01-26 오후 2:45
 * @author  이주희
 *
 * @변경이력
 **/
public interface ImageRepository extends JpaRepository<Image, Long> {

}
