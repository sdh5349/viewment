package com.web.curation.controller.memory;

import com.web.curation.dto.memory.MemoryDto;
import com.web.curation.repository.memory.MemoryRepository;
import com.web.curation.service.memory.MemoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * com.web.curation.controller.memory
 * MemoryController.java
 * @date    2021-01-27 오후 3:22
 * @author  김종성
 *
 * @변경이력
 **/
@Api(tags = {"3. Memory"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemoryController {

    private final MemoryService memoryService;

    @ApiOperation(value = "회원의 모든 기억하기 가져오기")
    @GetMapping("/users/{userId}/memories")
    public ResponseEntity<?> getMemories(@PathVariable("userId") String userId){
        List<MemoryDto> memories = memoryService.getMemories(userId);
        return ResponseEntity.ok().body(memories);
    }

    @ApiOperation(value = "핀이 있는 곳에 회원의 기억하기 추가")
    @PostMapping("/users/{userId}/memories/pin")
    public ResponseEntity<?> createMemoryWithPin(@PathVariable("userId") String userId,@RequestBody MemoryDto memoryDto){
        memoryService.createWithPin(userId, memoryDto, memoryDto.getPinId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "핀이 없는 곳에 회원의 기억하기 추가")
    @PostMapping("/users/{userId}/memories")
    public ResponseEntity<?> createMemory(@PathVariable("userId") String userId,@RequestBody MemoryDto memoryDto){
        memoryService.createWithoutPin(userId, memoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "회원의 기억하기 삭제")
    @DeleteMapping("/memories/{memoryId}")
    public ResponseEntity<?> deleteMemory(@PathVariable("memoryId") Long memoryId ){
        memoryService.deleteMemory(memoryId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원의 기억하기 수정")
    @PutMapping("/memories/{memoryId}")
    public ResponseEntity<?> modifyMemory(@PathVariable("memoryId") Long memoryId, @RequestBody MemoryDto memoryDto ){
        memoryService.updateMemory(memoryDto);
        return ResponseEntity.ok().build();
    }


}
