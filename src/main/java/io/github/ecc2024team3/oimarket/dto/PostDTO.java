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
    private Long postId;
    private Long userId;
    private String title;
    private String location;
    private Integer price;
    private String transactionStatus;
    private String content;
    private String representativeImage;
    private int likesCount;
    private int bookmarksCount;
    private boolean liked;
    private boolean bookmarked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PostDTO(Post post, String representativeImage) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getUserId();
        this.title = post.getTitle();
        this.location = post.getLocation();
        this.price = post.getPrice();
        this.transactionStatus = post.getTransactionStatus().name();
        this.content = post.getContent();
        this.representativeImage = representativeImage;  // Using passed representative image
        this.likesCount = 0;
        this.bookmarksCount = 0;
        this.liked = false;
        this.bookmarked = false;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
