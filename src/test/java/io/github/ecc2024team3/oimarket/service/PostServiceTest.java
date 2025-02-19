package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")  // ✅ H2 환경에서 테스트 실행
@Transactional
@Rollback(value = true)  // ✅ 테스트 실행 후 DB 상태 롤백
public class PostServiceTest {

    @Autowired
    private PostService postService;

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

        // ✅ 저장 후 다시 조회하여 userId 가져오기
        this.userId = userRepository.findByEmail("test@example.com")
                .map(User::getUserId)
                .orElseThrow(() -> new RuntimeException("사용자 저장 실패"));
    }

    @Test
    void testCreatePost() {
        // ✅ 테스트할 Post 데이터 생성
        PostCreateDTO postCreateDTO = PostCreateDTO.builder()
            .title("테스트 게시글")
            .location("서울")
            .price(10000)
            .transactionStatus("ON_SALE")
            .category("HOME_FOOD")
            .content("테스트 내용입니다.")
            .images(new ArrayList<>())  // ✅ 빈 리스트 추가
            .build();
    
        // ✅ PostService를 사용하여 게시글 생성
        PostDTO createdPost = postService.createPost(postCreateDTO, userId);
    
        // ✅ 테스트 검증
        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTitle()).isEqualTo("테스트 게시글");
        assertThat(createdPost.getLocation()).isEqualTo("서울");
        assertThat(createdPost.getPrice()).isEqualTo(10000);
        assertThat(createdPost.getTransactionStatus()).isEqualTo("ON_SALE");
        assertThat(createdPost.getCategory()).isEqualTo("HOME_FOOD");
        assertThat(createdPost.getContent()).isEqualTo("테스트 내용입니다.");
    }    
}