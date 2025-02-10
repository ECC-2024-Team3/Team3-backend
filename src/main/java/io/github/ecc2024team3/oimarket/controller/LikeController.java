package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{post_id}/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // ✅ 좋아요 등록/취소 (POST /api/posts/{post_id}/likes)
    @PostMapping
    public ResponseEntity<String> toggleLike(@PathVariable Long post_id,
                                             @RequestParam Long user_id) {
        boolean liked = likeService.toggleLike(user_id, post_id);
        return liked
                ? ResponseEntity.ok("좋아요가 추가되었습니다.")
                : ResponseEntity.ok("좋아요가 취소되었습니다.");
    }    
}
