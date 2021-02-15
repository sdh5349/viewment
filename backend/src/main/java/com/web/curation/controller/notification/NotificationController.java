package com.web.curation.controller.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.curation.dto.notification.NotiDto;
import com.web.curation.service.notification.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Notifications"})
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notis")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "푸시 알림 활성화")
    @PostMapping({""})
    public ResponseEntity<?> subscribe(@RequestBody NotiDto notiDto) throws FirebaseMessagingException {
        notificationService.subscribe(notiDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "푸시 알림 비활성화")
    @DeleteMapping({""})
    public ResponseEntity<?> unsubscribe(@RequestBody NotiDto notiDto) throws FirebaseMessagingException {
        notificationService.unsubscribe(notiDto);
        return ResponseEntity.ok().build();
    }
}
