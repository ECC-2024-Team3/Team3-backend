package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.Like;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDTO {
    private Long like_id;
    private Long user_id;
    private Long post_id;

    // ✅ 엔티티 → DTO 변환 생성자 추가
    public LikeDTO(Like like) {
        this.like_id = like.getLike_id();
        this.user_id = like.getUser().getUser_id();
        this.post_id = like.getPost().getPost_id();
    }
}