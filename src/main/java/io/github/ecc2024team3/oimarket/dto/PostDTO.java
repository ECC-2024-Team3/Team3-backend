package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long post_id;
    private Long user_id;
    private String title;
    private String location;
    private Integer price;
    private String transaction_status;
    private String content;
    private String representative_image;
    private int likes_count;
    private int bookmarks_count;
    private boolean liked; 
    private boolean bookmarked;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    // ✅ Post 엔티티 → PostDTO 변환
    public PostDTO(Post post) {
        this.post_id = post.getPost_id();
        this.user_id = post.getUser().getUser_id();
        this.title = post.getTitle();
        this.location = post.getLocation();
        this.price = post.getPrice();
        this.transaction_status = post.getTransaction_status();
        this.content = post.getContent();
        this.created_at = post.getCreated_at();
        this.updated_at = post.getUpdated_at();
    }
}