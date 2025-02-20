package io.github.ecc2024team3.oimarket.config;

import io.github.ecc2024team3.oimarket.entity.*;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // ✅ 직접 생성

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) { // 기존 데이터가 없을 때만 실행
            User user = User.builder()
                    .email("testuser@example.com")
                    .password(passwordEncoder.encode("password123")) // ✅ 여기서 직접 인코딩
                    .nickname("testuser")
                    .profileImage("https://example.com/profile1.jpg")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

            Post post = Post.builder()
                    .user(user)
                    .title("2019년형 불 들어오는 맥북 프로")
                    .location("학생문화관")
                    .price(1200000)
                    .transactionStatus(TransactionStatus.ON_SALE)
                    .category(Category.HOME_FOOD)
                    .itemCondition(ItemCondition.LIGHTLY_USED)
                    .content("사용감 좀 있어요! 잘 작동합니다!")
                    .createdAt(LocalDateTime.now())
                    .images(List.of()) // 이미지 리스트 비어있음
                    .build();

            postRepository.save(post);

            System.out.println("users, posts DB 삽입 완료");
        }
    }
}
