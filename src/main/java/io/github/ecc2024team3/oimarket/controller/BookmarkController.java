package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.service.BookmarkService;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final JwtTokenProvider jwtTokenProvider;

    public BookmarkController(BookmarkService bookmarkService, JwtTokenProvider jwtTokenProvider) {
        this.bookmarkService = bookmarkService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ✅ 북마크 등록/취소 (POST /api/posts/{postId}/bookmarks)
    @PostMapping
    public ResponseEntity<String> toggleBookmark(@PathVariable Long postId,
                                                 @RequestHeader("Authorization") String token) {

        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        boolean bookmarked = bookmarkService.toggleBookmark(userId, postId);
        return bookmarked
                ? ResponseEntity.ok("북마크가 추가되었습니다.")
                : ResponseEntity.ok("북마크가 취소되었습니다.");
    }
}
