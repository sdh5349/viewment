package com.web.curation.controller.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.curation.dto.notification.NotiSettingsDto;
import com.web.curation.dto.notification.UserNotiDto;
import com.web.curation.service.notification.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Notifications"})
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notis")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation(value = "푸시 알림 전체 활성화")
    @PostMapping({""})
    public ResponseEntity<?> subscribeAll(@RequestBody NotiSettingsDto notiSettingsDto) throws FirebaseMessagingException {
        notificationService.subscribeAll(notiSettingsDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "푸시 알림 정보 조회")
    @GetMapping({"/{userId}"})
    public ResponseEntity<?> getNotiInfo(@PathVariable("userId") String userId){
        UserNotiDto dto = notificationService.getNotiInfo(userId);
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "푸시 알림 활성화")
    @PutMapping({"/on"})
    public ResponseEntity<?> subscribe(@RequestBody NotiSettingsDto notiSettingsDto) throws FirebaseMessagingException {
        notificationService.subscribe(notiSettingsDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "푸시 알림 비활성화")
    @PutMapping({"/off"})
    public ResponseEntity<?> unsubscribe(@RequestBody NotiSettingsDto notiSettingsDto) throws FirebaseMessagingException {
        notificationService.unsubscribe(notiSettingsDto);
        return ResponseEntity.ok().build();
    }
}
