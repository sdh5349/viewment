package com.web.curation.controller.reply;

import com.web.curation.dto.reply.ReplyDto;
import com.web.curation.dto.reply.RereplyDto;
import com.web.curation.service.reply.ReplyService;
import com.web.curation.service.reply.RereplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.web.curation.controller.reply
 * ReplyController.java
 * @date    2021-02-02 오후 3:22
 * @author  이주희
 *
 * @변경이력
 **/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/replies")
public class ReplyController {

    private final ReplyService replyService;
    private final RereplyService rereplyService;

    @PostMapping("/reply")
    public ResponseEntity<?> writeReply(@RequestBody ReplyDto replyDto) throws Exception {
        replyService.writeReply(replyDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rereply")
    public ResponseEntity<?> writeRereply(@RequestBody RereplyDto rereplyDto) throws Exception {
        rereplyService.writeRereply(rereplyDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reply")
    public ResponseEntity<?> updateReply(@RequestBody ReplyDto replyDto) throws Exception {
        replyService.updateReply(replyDto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/rereply")
    public ResponseEntity<?> updateRereply(@RequestBody RereplyDto rereplyDto) throws Exception {
        rereplyService.updateRereply(rereplyDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rereply/{rereplyId}")
    public ResponseEntity<?> deleteReply(@PathVariable("rereplyId") Long rereplyId) throws Exception {
        rereplyService.deleteRereply(rereplyId);
        return ResponseEntity.ok().build();
    }
}
