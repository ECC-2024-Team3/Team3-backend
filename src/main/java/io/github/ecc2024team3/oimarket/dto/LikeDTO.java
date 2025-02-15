package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.Like;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDTO {
    private Long likeId;
    private Long userId;
    private Long postId;

    // ✅ 엔티티 → DTO 변환 생성자 추가
    public LikeDTO(Like like) {
        this.likeId = like.getLikeId();
        this.userId = like.getUser().getUserId();
        this.postId = like.getPost().getPostId();
    }
}