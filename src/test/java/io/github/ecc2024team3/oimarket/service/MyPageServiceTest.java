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
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import jakarta.transaction.Transactional;

import java.util.ArrayList;

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
    private CommentService commentService;

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
    private final int DEFAULT_PAGE_SIZE = 10;

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
                .category("HOME_FOOD")
                .condition("NEW")
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
        commentService.createComment(postId, userId, createCommentDTO); 

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
        Page<PostDTO> userPosts = myPageService.getUserPosts(userId, 0, DEFAULT_PAGE_SIZE);
        assertThat(userPosts.getContent()).isNotNull();
        assertThat(userPosts.getContent()).isNotEmpty();
    }

    @Test
    void testGetUserComments() {
        Page<CommentDTO> userComments = myPageService.getUserComments(userId, 0, 10);
        assertThat(userComments.getContent()).isNotNull();
        assertThat(userComments.getContent()).isNotEmpty();
        assertThat(userComments.getContent().get(0).getContent()).isEqualTo("테스트 댓글");
    }

    @Test
    void testDeleteAllLikedPosts() {
        // ✅ 좋아요 추가 확인
        Page<PostDTO> likedPosts = myPageService.getLikedPosts(userId, 0, DEFAULT_PAGE_SIZE);
        assertThat(likedPosts.getContent()).isNotEmpty();

        // ✅ 좋아요 전체 삭제
        myPageService.deleteAllLikedPosts(userId);

        // ✅ 삭제 후 확인
        Page<PostDTO> afterDelete = myPageService.getLikedPosts(userId, 0, DEFAULT_PAGE_SIZE);
        assertThat(afterDelete.getContent()).isEmpty();
    }

    @Test
    void testDeleteAllBookmarkedPosts() {
        // ✅ 북마크 추가 확인
        Page<PostDTO> bookmarkedPosts = myPageService.getBookmarkedPosts(userId, 0, DEFAULT_PAGE_SIZE);
        assertThat(bookmarkedPosts.getContent()).isNotEmpty();

        // ✅ 북마크 전체 삭제
        myPageService.deleteAllBookmarkedPosts(userId);

        // ✅ 삭제 후 확인
        Page<PostDTO> afterDelete = myPageService.getBookmarkedPosts(userId, 0, DEFAULT_PAGE_SIZE);
        assertThat(afterDelete.getContent()).isEmpty();
    }
}
