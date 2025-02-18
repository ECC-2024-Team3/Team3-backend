package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.CreateCommentDTO;
import io.github.ecc2024team3.oimarket.dto.UpdateCommentDTO;
import io.github.ecc2024team3.oimarket.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ✅ 로그인한 사용자의 ID 가져오는 메서드
    private Long getCurrentUserId(@AuthenticationPrincipal String username) {
        return commentService.getCurrentUserId(username);
    }

    // ✅ 댓글 생성 (postId 추가)
    @PostMapping("/{postId}")
    public ResponseEntity<CommentDTO> createComment(
            @AuthenticationPrincipal String username,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentDTO dto) {
        Long userId = getCurrentUserId(username);
        return ResponseEntity.ok(commentService.createComment(userId, postId, dto));
    }

    // ✅ 특정 게시글의 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    // ✅ 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @AuthenticationPrincipal String username,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentDTO dto) {
        Long userId = getCurrentUserId(username);
        CommentDTO updatedComment = commentService.updateComment(userId, commentId, dto);
        return ResponseEntity.ok(updatedComment);
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal String username,
            @PathVariable Long commentId) {
        Long userId = getCurrentUserId(username);
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}
