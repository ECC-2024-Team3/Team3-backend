package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    // 현재 로그인한 사용자 ID 검증
    private Long getAuthenticatedUserId(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authenticatedUserId = Long.parseLong(authentication.getName());
        if (!authenticatedUserId.equals(userId)) {
            throw new SecurityException("이 작업을 수행할 권한이 없습니다.");
        }
        return authenticatedUserId;
    }

    // 게시글 조회, 삭제, 수정

    // ✅ 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<Long>> getUserPosts(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        return ResponseEntity.ok(myPageService.getUserPosts(userId));
    }

    // ✅ 선택한 게시글 삭제
    @DeleteMapping("/posts")
    public ResponseEntity<Void> deleteSelectedUserPosts(@RequestParam Long userId, @RequestBody List<Long> postIds) {
        getAuthenticatedUserId(userId);
        myPageService.deleteSelectedUserPosts(userId, postIds);
        return ResponseEntity.noContent().build();
    }

    // ✅ 모든 게시글 삭제
    @DeleteMapping("/posts/all")
    public ResponseEntity<Void> deleteAllUserPosts(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        myPageService.deleteAllUserPosts(userId);
        return ResponseEntity.noContent().build();
    }

    // ✅ 게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> updateMyPost(@RequestParam Long userId, @PathVariable Long postId, @RequestBody PostUpdateDTO postUpdateDTO) {
        getAuthenticatedUserId(userId);
        return ResponseEntity.ok(myPageService.updateMyPost(userId, postId, postUpdateDTO));
    }

    // 좋아요 조회, 삭제

    // ✅ 좋아요한 게시글 조회
    @GetMapping("/likes")
    public ResponseEntity<List<Long>> getLikedPosts(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        return ResponseEntity.ok(myPageService.getLikedPosts(userId));
    }

    // ✅ 선택한 좋아요 해제
    @DeleteMapping("/likes")
    public ResponseEntity<Void> deleteSelectedLikedPosts(@RequestParam Long userId, @RequestBody List<Long> selectedPostIds) {
        getAuthenticatedUserId(userId);
        myPageService.deleteSelectedLikedPosts(userId, selectedPostIds);
        return ResponseEntity.noContent().build();
    }

    // ✅ 모든 좋아요 해제
    @DeleteMapping("/likes/all")
    public ResponseEntity<Void> deleteAllLikedPosts(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        myPageService.deleteAllLikedPosts(userId);
        return ResponseEntity.noContent().build();
    }
// 북마크 조회, 삭제

    // ✅ 북마크한 게시글 조회
    @GetMapping("/bookmarks")
    public ResponseEntity<List<Long>> getBookmarkedPosts(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        return ResponseEntity.ok(myPageService.getBookmarkedPosts(userId));
    }

    // ✅ 선택한 북마크 해제
    @DeleteMapping("/bookmarks")
    public ResponseEntity<Void> deleteSelectedBookmarkedPosts(@RequestParam Long userId,
                                                              @RequestBody List<Long> selectedPostIds) {
        getAuthenticatedUserId(userId);
        myPageService.deleteSelectedBookmarkedPosts(userId, selectedPostIds);
        return ResponseEntity.noContent().build();
    }

    // ✅ 모든 북마크 해제
    @DeleteMapping("/bookmarks/all")
    public ResponseEntity<Void> deleteAllBookmarkedPosts(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        myPageService.deleteAllBookmarkedPosts(userId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 조회, 삭제

    // ✅ 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDTO>> getUserComments(@RequestParam Long userId) {
        getAuthenticatedUserId(userId);
        return ResponseEntity.ok(myPageService.getUserComments(userId));
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteUserComment(@RequestParam Long userId, @PathVariable Long commentId) {
        getAuthenticatedUserId(userId);
        myPageService.deleteUserComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
