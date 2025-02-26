package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.CreateCommentDTO;
import io.github.ecc2024team3.oimarket.dto.UpdateCommentDTO;
import io.github.ecc2024team3.oimarket.service.CommentService;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    // ✅ 댓글 생성 (postId 추가)
    @PostMapping("/{postId}")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentDTO dto,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(commentService.createComment(userId, postId, dto));
    }

    // ✅ 특정 게시글의 댓글 조회 (페이징 적용)
    @GetMapping("/post/{postId}")
    public ResponseEntity<Page<CommentDTO>> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {  // 기본 size=10
        return ResponseEntity.ok(commentService.getCommentsByPost(postId, page, size));
    }

    // ✅ 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentDTO dto,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        CommentDTO updatedComment = commentService.updateComment(userId, commentId, dto);
        return ResponseEntity.ok(updatedComment);
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}