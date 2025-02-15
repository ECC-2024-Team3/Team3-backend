package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.BookmarkRepository;
import io.github.ecc2024team3.oimarket.repository.LikeRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // ✅ H2 환경에서 테스트 실행
@Transactional
@Rollback(value = true)  // ✅ 테스트 실행 후 DB 상태 롤백
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private BookmarkRepository bookmarkRepository;

    @MockBean
    private LikeRepository likeRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    private Long user_id;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setNickname("testuser");
        userRepository.save(user);
        user_id = user.getUser_id();
    }

    @Test
    void testCreatePost() {
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("테스트 게시글");
        postCreateDTO.setLocation("서울");
        postCreateDTO.setPrice(10000);
        postCreateDTO.setTransaction_status("판매_중");
        postCreateDTO.setContent("테스트 내용입니다.");

        PostDTO createdPost = postService.createPost(postCreateDTO, user_id);

        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTitle()).isEqualTo("테스트 게시글");
        assertThat(createdPost.getLocation()).isEqualTo("서울");
        assertThat(createdPost.getPrice()).isEqualTo(10000);
        assertThat(createdPost.getTransaction_status()).isEqualTo("판매_중");
        assertThat(createdPost.getContent()).isEqualTo("테스트 내용입니다.");
    }
}
