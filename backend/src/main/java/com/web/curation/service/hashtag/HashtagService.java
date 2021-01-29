package com.web.curation.service.hashtag;

import com.web.curation.domain.hashtag.Hashtag;
import com.web.curation.dto.hashtag.HashtagDto;
import com.web.curation.repository.hashtag.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * com.web.curation.service.hashtag
 * HashtagService.java
 * @date    2021-01-27
 * @author  이주희
 *
 * @변경이력
 **/

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public List<HashtagDto> findByContentsStartingWithIgnoreCase(String contents) {
        List<HashtagDto> result = new ArrayList<>();
        List<Hashtag> findhashtags = hashtagRepository.findByContentsStartingWithIgnoreCase(contents);
        for(Hashtag hashtag : findhashtags){
            result.add(new HashtagDto(hashtag));
        }
        return result;
    }
}
