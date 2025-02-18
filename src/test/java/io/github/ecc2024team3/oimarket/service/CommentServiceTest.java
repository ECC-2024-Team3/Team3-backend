package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.CreateCommentDTO;
import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import io.github.ecc2024team3.oimarket.repository.CommentRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;

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
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Long userId;
    private Long postId;

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

        // ✅ 테스트용 게시글 생성
        PostCreateDTO postCreateDTO = PostCreateDTO.builder()
                .title("테스트 게시글")
                .location("서울")
                .price(10000)
                .transactionStatus("ON_SALE")
                .content("테스트 내용입니다.")
                .images(new ArrayList<>())
                .build();

        // ✅ 게시글 저장 및 postId 가져오기
        PostDTO createdPost = postService.createPost(postCreateDTO, userId);
        this.postId = createdPost.getPostId();
    }

    @Test
    void testCreateComment() {
        // ✅ 사용자 ID와 게시글 ID가 필요
        Long postId = this.postId; // 테스트용 게시글 ID 가져오기
        Long userId = this.userId; // 미리 저장한 사용자 ID 사용

        // ✅ CreateCommentDTO 객체 생성
        CreateCommentDTO createCommentDTO = CreateCommentDTO.builder()
                .content("테스트 댓글입니다.")  // ✅ 올바른 데이터 추가
                .build();

        // ✅ 댓글 생성
        CommentDTO createdComment = commentService.createComment(userId, postId, createCommentDTO);

        // ✅ 검증
        assertThat(createdComment).isNotNull();
        assertThat(createdComment.getContent()).isEqualTo("테스트 댓글입니다.");
    }
}
