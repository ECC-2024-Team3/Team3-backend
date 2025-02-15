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

    // ✅ 기존 `Post`만을 매개변수로 받는 생성자 => create, update에서 사용
    public PostDTO(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getUserId();
        this.title = post.getTitle();
        this.location = post.getLocation();
        this.price = post.getPrice();
        this.transactionStatus = post.getTransactionStatus().name();
        this.content = post.getContent();
        this.representativeImage = (post.getImages() != null && !post.getImages().isEmpty()) 
            ? post.getImages().get(0).getImageUrl() 
            : null;
        this.likesCount = 0;
        this.bookmarksCount = 0;
        this.liked = false;
        this.bookmarked = false;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    // ✅ `liked`, `bookmarked`, `likesCount`, `bookmarksCount` 포함하는 생성자 => read, search
    public PostDTO(Post post, boolean liked, boolean bookmarked, int likesCount, int bookmarksCount) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getUserId();
        this.title = post.getTitle();
        this.location = post.getLocation();
        this.price = post.getPrice();
        this.transactionStatus = post.getTransactionStatus().name();
        this.content = post.getContent();
        this.representativeImage = (post.getImages() != null && !post.getImages().isEmpty()) 
            ? post.getImages().get(0).getImageUrl() 
            : null;        
        this.likesCount = likesCount;
        this.bookmarksCount = bookmarksCount;
        this.liked = liked;
        this.bookmarked = bookmarked;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}