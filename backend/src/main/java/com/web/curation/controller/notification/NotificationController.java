package com.web.curation.controller.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.web.curation.dto.notification.NotiListDto;
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
    public ResponseEntity<UserNotiDto> getNotiInfo(@PathVariable("userId") String userId){
        UserNotiDto dto = notificationService.getNotiInfo(userId);
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "확인하지 않은 알림 개수 조회")
    @GetMapping({"/uncheckcnt/{userId}"})
    public ResponseEntity<Long> getCountUncheckNoti(@PathVariable("userId") String userId){
        long cnt = notificationService.getCountUncheckNoti(userId);
        return ResponseEntity.ok().body(cnt);
    }

    @ApiOperation(value = "확인하지 않은 알림 목록 조회")
    @GetMapping({"/unchecklist/{userId}"})
    public ResponseEntity<List<NotiListDto>> getUncheckNoti(@PathVariable("userId") String userId){
        List<NotiListDto> result = notificationService.getUncheckNoti(userId);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "확인한 알림 목록 조회")
    @GetMapping({"/checklist/{userId}"})
    public ResponseEntity<List<NotiListDto>> getcheckNoti(@PathVariable("userId") String userId){
        List<NotiListDto> result = notificationService.getcheckNoti(userId);
        return ResponseEntity.ok().body(result);
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
