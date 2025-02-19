package io.github.ecc2024team3.oimarket.entity;

import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private String title;
    private String location;
    private Integer price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    public Post(PostCreateDTO postDTO, User user) {
        this.user = user;
        this.title = postDTO.getTitle();
        this.location = postDTO.getLocation();
        this.price = postDTO.getPrice();
        this.transactionStatus = TransactionStatus.valueOf(postDTO.getTransactionStatus());
        this.category = Category.valueOf(postDTO.getCategory());
        this.condition = Condition.valueOf(postDTO.getCondition());
        this.content = postDTO.getContent();
        this.createdAt = LocalDateTime.now();
    }
}