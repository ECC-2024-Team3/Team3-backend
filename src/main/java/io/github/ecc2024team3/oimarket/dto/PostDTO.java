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

    // ✅ 기존 `Post`만을 매개변수로 받는 생성자 => create, update에서 사용
    public PostDTO(Post post) {
        this.post_id = post.getPost_id();
        this.user_id = post.getUser().getUser_id();
        this.title = post.getTitle();
        this.location = post.getLocation();
        this.price = post.getPrice();
        this.transaction_status = post.getTransaction_status();
        this.content = post.getContent();
        this.representative_image = post.getImages().isEmpty() ? null : post.getImages().get(0).getImage_url();
        this.likes_count = 0;
        this.bookmarks_count = 0;
        this.liked = false;
        this.bookmarked = false;
        this.created_at = post.getCreated_at();
        this.updated_at = post.getUpdated_at();
    }

    // ✅ `liked`, `bookmarked`, `likesCount`, `bookmarksCount` 포함하는 생성자 => read, search에서 사용
    public PostDTO(Post post, boolean liked, boolean bookmarked, int likesCount, int bookmarksCount) {
        this.post_id = post.getPost_id();
        this.user_id = post.getUser().getUser_id();
        this.title = post.getTitle();
        this.location = post.getLocation();
        this.price = post.getPrice();
        this.transaction_status = post.getTransaction_status();
        this.content = post.getContent();
        this.representative_image = post.getImages().isEmpty() ? null : post.getImages().get(0).getImage_url();
        this.likes_count = likesCount;
        this.bookmarks_count = bookmarksCount;
        this.liked = liked;
        this.bookmarked = bookmarked;
        this.created_at = post.getCreated_at();
        this.updated_at = post.getUpdated_at();
    }
}