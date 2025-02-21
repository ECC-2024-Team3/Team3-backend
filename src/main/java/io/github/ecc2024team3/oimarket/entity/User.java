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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 32)
    private String email;

    @Column(nullable = false, length = 60) // 비밀번호 해싱을 고려하여 적절한 길이 설정
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String nickname;

    @Column(length = 500) // NULL 가능 (프로필 이미지가 없을 수도 있음)
    private String profileImage;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
