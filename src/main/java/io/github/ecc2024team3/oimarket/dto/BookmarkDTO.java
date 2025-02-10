package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.Bookmark;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkDTO {
    private Long bookmark_id;
    private Long user_id;
    private Long post_id;

    // ✅ 엔티티 → DTO 변환 생성자 추가
    public BookmarkDTO(Bookmark bookmark) {
        this.bookmark_id = bookmark.getBookmark_id();
        this.user_id = bookmark.getUser().getUser_id();
        this.post_id = bookmark.getPost().getPost_id();
    }
}
