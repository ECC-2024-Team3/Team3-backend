package io.github.ecc2024team3.oimarket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bookmarks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmark_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();

    // ✅ DTO → 엔티티 변환 생성자 추가
    public Bookmark(User user, Post post) {
        this.user = user;
        this.post = post;
        this.created_at = LocalDateTime.now();
    }
}