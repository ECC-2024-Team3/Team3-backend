package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.Bookmark;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDTO {
    private Long bookmarkId;
    private Long userId;
    private Long postId;

    // ✅ 엔티티 → DTO 변환 생성자 추가
    public BookmarkDTO(Bookmark bookmark) {
        this.bookmarkId = bookmark.getBookmarkId();
        this.userId = bookmark.getUser().getUserId();
        this.postId = bookmark.getPost().getPostId();
    }
}