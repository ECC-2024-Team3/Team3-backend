package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.UserDTO;
import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.service.MyPageService;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //비밀번호 해싱
    private final JwtTokenProvider jwtTokenProvider;


    // 사용자가 작성한 모든 게시글 조회 (페이징 적용)
    @GetMapping("/posts")
    public ResponseEntity<Page<PostDTO>> getUserPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,// 기본값 size=10
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(myPageService.getUserPosts(userId, page, size));
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts")
    public ResponseEntity<Void> deleteSelectedPosts(
            @RequestParam List<Long> postIds,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteSelectedPosts(userId, postIds);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }


    // 모든 게시글 삭제
    @DeleteMapping("/posts/all")
    public ResponseEntity<Void> deleteAllUserPosts(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteAllUserPosts(userId); // 게시글 삭제 서비스 호출
        return ResponseEntity.noContent().build(); // 삭제 완료 응답
    }

    // 게시글 수정
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> updateMyPost(
            @PathVariable Long postId,
            @RequestBody PostUpdateDTO postUpdateDTO,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(myPageService.updateMyPost(userId, postId, postUpdateDTO));
    }

    // 사용자가 좋아요한 게시글 조회 (페이징 적용)
    @GetMapping("/likes")
    public ResponseEntity<Page<PostDTO>> getLikedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,    // 기본값 size=10
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(myPageService.getLikedPosts(userId, page, size));
    }

    // 선택한 좋아요 해제
    @DeleteMapping("/likes")
    public ResponseEntity<Void> deleteSelectedLikedPosts(
            @RequestParam List<Long> likeIds,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteSelectedLikedPosts(userId, likeIds);
        return ResponseEntity.noContent().build();
    }

    // 모든 좋아요 해제
    @DeleteMapping("/likes/all")
    public ResponseEntity<Void> deleteAllLikedPosts(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteAllLikedPosts(userId); // 좋아요 해제
        return ResponseEntity.noContent().build();
    }

    // 사용자가 북마크한 게시글 조회 (페이징 적용)
    @GetMapping("/bookmarks")
    public ResponseEntity<Page<PostDTO>> getBookmarkedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(myPageService.getBookmarkedPosts(userId, page, size));
    }

    // 선택한 북마크 해제
    @DeleteMapping("/bookmarks")
    public ResponseEntity<Void> deleteSelectedBookmarkedPosts(
            @RequestParam List<Long> bookmarkIds,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteSelectedBookmarkedPosts(userId, bookmarkIds);
        return ResponseEntity.noContent().build();
    }

    //모든 북마크 해제
    @DeleteMapping("/bookmarks/all")
    public ResponseEntity<Void> deleteAllBookmarkedPosts(
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteAllBookmarkedPosts(userId); // 북마크 해제
        return ResponseEntity.noContent().build();
    }

    //댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentDTO>> getUserComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(myPageService.getUserComments(userId, page, size));
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteUserComment(
            @PathVariable Long commentId, @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        myPageService.deleteUserComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 사용자 정보 조회 (GET)
    @GetMapping("/info")
    public ResponseEntity<UserDTO> getUserInfo(Authentication authentication) {
        String email = authentication.getName(); // JWT에서 사용자 이메일 가져오기
        return ResponseEntity.ok(myPageService.getUserInfo(email));
    }

    // 사용자 정보 등록 (POST)
    @PostMapping("/info")
    public ResponseEntity<UserDTO> createUserInfo(@RequestBody UserDTO userDTO, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(myPageService.createUserInfo(email, userDTO));
    }

    // 사용자 정보 수정 (PATCH)
    @PatchMapping("/info")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO, Authentication authentication) {
        String email = authentication.getName();

        if (userDTO.getPassword() != null) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword())); // 비밀번호 해싱 후 저장
        }

        return ResponseEntity.ok(myPageService.updateUserInfo(email, userDTO));
    }

    //  사용자 정보 삭제 (DELETE)
    @DeleteMapping("/info")
    public ResponseEntity<Void> deleteUserInfo(Authentication authentication) {
        String email = authentication.getName();
        myPageService.deleteUserInfo(email);
        return ResponseEntity.noContent().build();
    }
}
