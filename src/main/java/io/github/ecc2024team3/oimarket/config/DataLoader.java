package io.github.ecc2024team3.oimarket.config;

import io.github.ecc2024team3.oimarket.entity.*;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import io.github.ecc2024team3.oimarket.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .email("testuser@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .nickname("testuser")
                    .profileImage("https://example.com/profile1.jpg")
                    .createdAt(LocalDateTime.now())
                    .build();

            User user2 = User.builder()
                    .email("eccteam3@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .nickname("eccteam3")
                    .profileImage("https://example.com/profile2.jpg")
                    .createdAt(LocalDateTime.now())
                    .build();

            User user3 = User.builder()
                    .email("ewha1886@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .nickname("ewha1886")
                    .profileImage("https://example.com/profile3.jpg")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.saveAll(List.of(user1, user2, user3));

            Post post1 = Post.builder()
                    .user(user1)
                    .title("2019년형 불 들어오는 맥북 프로")
                    .location("학생문화관")
                    .price(1200000)
                    .transactionStatus(TransactionStatus.ON_SALE)
                    .category(Category.HOME_FOOD)
                    .itemCondition(ItemCondition.LIGHTLY_USED)
                    .content("사용감 좀 있어요! 잘 작동합니다!")
                    .createdAt(LocalDateTime.now())
                    .images(List.of())
                    .build();

            Post post2 = Post.builder()
                    .user(user3) // ewha1886이 작성한 게시글
                    .title("갤럭시 S23 울트라 팝니다")
                    .location("학교 정문")
                    .price(800000)
                    .transactionStatus(TransactionStatus.RESERVED)
                    .category(Category.HOME_FOOD)
                    .itemCondition(ItemCondition.NEW)
                    .content("거의 새것입니다. 박스도 있어요.")
                    .createdAt(LocalDateTime.now())
                    .images(List.of())
                    .build();

            postRepository.saveAll(List.of(post1, post2));

            Comment comment1 = Comment.builder()
                    .user(user2) // eccteam3
                    .post(post2) // 갤럭시 S23 게시글
                    .content("안녕하세요! 물건 아직 남아있나요?")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Comment comment2 = Comment.builder()
                    .user(user3) // ewha1886
                    .post(post2) // 갤럭시 S23 게시글
                    .content("네 있습니다~")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            commentRepository.saveAll(List.of(comment1, comment2));

            System.out.println("users, posts, comments DB 삽입 완료");
        }
    }
}
