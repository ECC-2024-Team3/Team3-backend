package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.service.LikeService;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;
    private final JwtTokenProvider jwtTokenProvider;

    public LikeController(LikeService likeService, JwtTokenProvider jwtTokenProvider) {
        this.likeService = likeService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ✅ 좋아요 등록/취소 (POST /api/posts/{postId}/likes)
    @PostMapping
    public ResponseEntity<String> toggleLike(@PathVariable Long postId,
                                             @RequestHeader("Authorization") String token) {

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        boolean liked = likeService.toggleLike(userId, postId);
        return liked
                ? ResponseEntity.ok("좋아요가 추가되었습니다.")
                : ResponseEntity.ok("좋아요가 취소되었습니다.");
    }      
}
