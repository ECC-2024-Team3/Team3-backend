package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.service.BookmarkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{post_id}/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    // ✅ 북마크 등록/취소 (POST /api/posts/{post_id}/bookmarks)
    @PostMapping
    public ResponseEntity<String> toggleBookmark(@PathVariable Long post_id,
                                                 @RequestParam Long user_id) {
        boolean bookmarked = bookmarkService.toggleBookmark(user_id, post_id);
        return bookmarked
                ? ResponseEntity.ok("북마크가 추가되었습니다.")
                : ResponseEntity.ok("북마크가 취소되었습니다.");
    }    
}