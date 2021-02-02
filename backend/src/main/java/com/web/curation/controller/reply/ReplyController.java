package com.web.curation.controller.reply;

import com.web.curation.dto.reply.ReplyDto;
import com.web.curation.dto.reply.RereplyDto;
import com.web.curation.service.reply.ReplyService;
import com.web.curation.service.reply.RereplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@Api(tags = {"Replies"})
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/replies")
public class ReplyController {

    private final ReplyService replyService;
    private final RereplyService rereplyService;

    @ApiOperation(value = "댓글 작성", notes="articleId, contetns, userId 필수")
    @PostMapping("")
    public ResponseEntity<?> writeReply(@RequestBody ReplyDto replyDto) throws Exception {
        replyService.writeReply(replyDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "대댓글 작성", notes="parentId(어느 댓글에 달리는 대댓글인지), contetns, userId 필수")
    @PostMapping("/rereply")
    public ResponseEntity<?> writeRereply(@RequestBody RereplyDto rereplyDto) throws Exception {
        rereplyService.writeRereply(rereplyDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "댓글 수정", notes="articleId, contetns, userId 필수")
    @PutMapping("")
    public ResponseEntity<?> updateReply(@RequestBody ReplyDto replyDto) throws Exception {
        replyService.updateReply(replyDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "대댓글 수정", notes="parentId, contetns, userId 필수")
    @PutMapping("/rereply")
    public ResponseEntity<?> updateRereply(@RequestBody RereplyDto rereplyDto) throws Exception {
        rereplyService.updateRereply(rereplyDto);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable("replyId") Long replyId) throws Exception {
        replyService.deleteReply(replyId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "대댓글 삭제")
    @DeleteMapping("/rereply/{rereplyId}")
    public ResponseEntity<?> deleteRereply(@PathVariable("rereplyId") Long rereplyId) throws Exception {
        rereplyService.deleteRereply(rereplyId);
        return ResponseEntity.ok().build();
    }
}
