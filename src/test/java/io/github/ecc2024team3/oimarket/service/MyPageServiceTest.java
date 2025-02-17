package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import jakarta.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // ✅ H2 환경에서 테스트 실행
@Transactional
@Rollback(value = true)  // ✅ 테스트 실행 후 DB 상태 롤백
public class MyPageServiceTest {

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private UserRepository userRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        // ✅ User 저장
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testuser")
                .build();
        userRepository.save(user);

        // ✅ 저장 후 userId 가져오기
        this.userId = userRepository.findByEmail("test@example.com")
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("사용자 저장 실패"));
    }

    @Test
    void testGetUserPosts() {
        List<Long> userPosts = myPageService.getUserPosts(userId);
        assertThat(userPosts).isNotNull();
    }

    @Test
    void testGetUserComments() {
        List<CommentDTO> userComments = myPageService.getUserComments(userId);
        assertThat(userComments).isNotNull();
    }
}
