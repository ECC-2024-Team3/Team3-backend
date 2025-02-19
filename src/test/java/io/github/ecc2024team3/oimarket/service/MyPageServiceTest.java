package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.CreateCommentDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.entity.Bookmark;
import io.github.ecc2024team3.oimarket.entity.Like;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.BookmarkRepository;
import io.github.ecc2024team3.oimarket.repository.LikeRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // ✅ H2 환경에서 테스트 실행
@Transactional
@RequiredArgsConstructor
@Rollback(value = true)  // ✅ 테스트 실행 후 DB 상태 롤백
public class MyPageServiceTest {

    @Autowired
    private MyPageService myPageService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService; // ✅ 댓글 서비스 추가

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private LikeRepository likeRepository;
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

        // ✅ 게시글 엔티티 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // ✅ 댓글 추가
        CreateCommentDTO createCommentDTO = CreateCommentDTO.builder()
                .content("테스트 댓글")
                .build();

        commentService.createComment(postId, userId, createCommentDTO); // ✅ 댓글 생성 코드 추가

        // ✅ 좋아요 추가
        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();
        likeRepository.save(like);

        // ✅ 북마크 추가
        Bookmark bookmark = Bookmark.builder()
                .post(post)
                .user(user)
                .build();
        bookmarkRepository.save(bookmark);
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
        assertThat(userComments).isNotEmpty(); // ✅ 댓글이 정상적으로 추가되었는지 확인
        assertThat(userComments.get(0).getContent()).isEqualTo("테스트 댓글");
    }

    @Test
    void testDeleteAllLikedPosts() {
        // 좋아요 추가 (가정)
        List<Long> likedPosts = myPageService.getLikedPosts(userId);
        assertThat(likedPosts).isNotEmpty(); // 좋아요 데이터가 존재하는지 확인

        // 좋아요 전체 삭제
        myPageService.deleteAllLikedPosts(userId);

        // 삭제 후 확인
        List<Long> afterDelete = myPageService.getLikedPosts(userId);
        assertThat(afterDelete).isEmpty(); // 좋아요가 삭제되었는지 확인
    }

    @Test
    void testDeleteAllBookmarkedPosts() {
        // 북마크 추가 (가정)
        List<Long> bookmarkedPosts = myPageService.getBookmarkedPosts(userId);
        assertThat(bookmarkedPosts).isNotEmpty(); // 북마크 데이터가 존재하는지 확인

        // 북마크 전체 삭제
        myPageService.deleteAllBookmarkedPosts(userId);

        // 삭제 후 확인
        List<Long> afterDelete = myPageService.getBookmarkedPosts(userId);
        assertThat(afterDelete).isEmpty(); // 북마크가 삭제되었는지 확인
    }
}