package com.web.curation.service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import com.web.curation.domain.Memory;
import com.web.curation.dto.notification.NotiDto;
import com.web.curation.exceptions.ElementNotFoundException;
import com.web.curation.repository.memory.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final MemoryRepository memoryRepository;

    public void subscribe(NotiDto notiDto) throws FirebaseMessagingException {
        List<String> tokenList = new ArrayList<>();
        tokenList.add(notiDto.getRegiToken());
        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(tokenList, "memory-" + notiDto.getMemoryId());
        Memory memory = memoryRepository.findById(notiDto.getMemoryId()).orElseThrow(
                ()->{ throw new ElementNotFoundException("Memory", notiDto.getMemoryId().toString());}
        );
        memory.setNotification(true);
    }

    public void unsubscribe(NotiDto notiDto) throws FirebaseMessagingException {
        List<String> tokenList = new ArrayList<>();
        tokenList.add(notiDto.getRegiToken());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(tokenList, "memory-" + notiDto.getMemoryId());
        Memory memory = memoryRepository.findById(notiDto.getMemoryId()).orElseThrow(
                ()->{ throw new ElementNotFoundException("Memory", notiDto.getMemoryId().toString());}
        );
        memory.setNotification(false);
    }

}
