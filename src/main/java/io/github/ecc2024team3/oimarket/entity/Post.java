package io.github.ecc2024team3.oimarket.entity;

import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    private String location;
    private Integer price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transaction_status;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private LocalDateTime created_at = LocalDateTime.now();
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    // ✅ PostDTO → Post 엔티티 변환
    public Post(PostCreateDTO postDTO, User user) {
        this.user = user;
        this.title = postDTO.getTitle();
        this.location = postDTO.getLocation();
        this.price = postDTO.getPrice();
        this.transaction_status = TransactionStatus.valueOf(postDTO.getTransaction_status());;
        this.content = postDTO.getContent();
        this.created_at = LocalDateTime.now();
    }
}
