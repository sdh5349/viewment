package com.web.curation.dto.hashtag;

import com.web.curation.domain.hashtag.Hashtag;
import lombok.Getter;
import lombok.Setter;

/**
 * com.web.curation.dto.hashtag
 * HashtagDto.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@Getter
@Setter
public class HashtagDto {
    private Long hashtagId;
    private String contents;

    public HashtagDto(Hashtag hashtag){
        hashtagId = hashtag.getHashtagId();
        contents = hashtag.getContents();
    }
}
