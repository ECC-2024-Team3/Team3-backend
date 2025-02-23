package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import io.github.ecc2024team3.oimarket.dto.*;
import io.github.ecc2024team3.oimarket.entity.Bookmark;
import io.github.ecc2024team3.oimarket.entity.Image;
import io.github.ecc2024team3.oimarket.entity.Like;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final PostService postService;
    private final CommentService commentService;


    // ✅ 사용자가 작성한 모든 게시글 조회
    @Transactional
    public Page<PostDTO> getUserPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10));
        Page<Post> posts = postRepository.findByUser(userId, pageable);

        return posts.map(post -> {
            List<Image> images = Optional.ofNullable(post.getImages()).orElse(Collections.emptyList());
            String representativeImage = images.isEmpty() ? null : images.get(0).getImageUrl();
            return new PostDTO(post, representativeImage);
        });
    }

    // ✅ 사용자가 선택한 게시글만 삭제
    @Transactional
    public void deleteSelectedPosts(Long userId, List<Long> postIds) {
        // 선택한 게시글을 조회
        List<Post> posts = postRepository.findAllById(postIds);
        postRepository.deleteAll(posts);
    }


    // 사용자의 모든 게시글 삭제
    @Transactional
    public void deleteAllUserPosts(Long userId) {
        Pageable pageable = PageRequest.of(0, 100);  // 한 번에 100개씩 가져오기 (적절히 조정)
        Page<Post> posts;

        // 전체 페이지를 조회하여 삭제
        do {
            posts = postRepository.findByUser(userId, pageable);
            posts.getContent().forEach(post -> postService.deletePost(post.getPostId(), userId)); // 게시글 삭제
            pageable = pageable.next(); // 다음 페이지로 이동
        } while (posts.hasNext()); // 더 이상 페이지가 없으면 종료
    }

    // ✅ 마이페이지에서 특정 게시글 수정하기
    @Transactional
    public PostDTO updateMyPost(Long userId, Long postId, PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(postId, userId, postUpdateDTO);
    }

    // ✅ 사용자가 좋아요한 게시글 목록 조회
    @Transactional
    public Page<PostDTO> getLikedPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10));
        Page<Like> likes = likeRepository.findByUserId(userId, pageable); // ✅ 좋아요한 게시글 조회 (페이징 적용)

        return likes.map(like -> {
            Post post = like.getPost();
            List<Image> images = Optional.ofNullable(post.getImages()).orElse(Collections.emptyList());
            String representativeImage = images.isEmpty() ? null : images.get(0).getImageUrl();
            return new PostDTO(post, representativeImage);
        });
    }

    // ✅ 사용자가 선택한 게시글만 좋아요 해제
    @Transactional
    public void deleteSelectedLikedPosts(Long userId, List<Long> likeIds) {
        List<Like> likes = likeRepository.findAllById(likeIds);
        likeRepository.deleteAll(likes);
    }

    // ✅ 사용자가 좋아요한 모든 게시글 좋아요 해제
    @Transactional
    public void deleteAllLikedPosts(Long userId) {
        // 페이지네이션 설정 (한 번에 100개씩 처리)
        Pageable pageable = PageRequest.of(0, 100);

        // 사용자가 좋아요한 모든 Like 조회 (페이징)
        Page<Like> likesPage = likeRepository.findByUserId(userId, pageable);

        // 모든 좋아요를 제거
        while (likesPage.hasContent()) {
            likeRepository.deleteAll(likesPage.getContent());  // 해당 페이지의 모든 좋아요 삭제
            likesPage = likeRepository.findByUserId(userId, pageable.next());  // 다음 페이지로 이동
        }
    }

    // ✅ 사용자가 북마크한 게시글 목록 조회
    @Transactional
    public Page<PostDTO> getBookmarkedPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10));
        Page<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId, pageable); // ✅ 북마크한 게시글 조회 (페이징 적용)

        return bookmarks.map(bookmark -> {
            Post post = bookmark.getPost();
            List<Image> images = Optional.ofNullable(post.getImages()).orElse(Collections.emptyList());
            String representativeImage = images.isEmpty() ? null : images.get(0).getImageUrl();
            return new PostDTO(post, representativeImage);
        });

    }

    // ✅ 사용자가 선택한 게시글만 북마크 해제
    @Transactional
    public void deleteSelectedBookmarkedPosts(Long userId, List<Long> bookmarkIds) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllById(bookmarkIds);
        bookmarkRepository.deleteAll(bookmarks);
    }

    // ✅ 사용자가 북마크한 모든 게시글 북마크 해제
    @Transactional
    public void deleteAllBookmarkedPosts(Long userId) {
        // 페이지네이션 설정 (한 번에 100개씩 처리)
        Pageable pageable = PageRequest.of(0, 100);

        // 사용자가 북마크한 모든 Bookmark 조회 (페이징)
        Page<Bookmark> bookmarksPage = bookmarkRepository.findByUserId(userId, pageable);

        // 모든 북마크를 제거
        while (bookmarksPage.hasContent()) {
            bookmarkRepository.deleteAll(bookmarksPage.getContent());  // 해당 페이지의 모든 북마크 삭제
            bookmarksPage = bookmarkRepository.findByUserId(userId, pageable.next());  // 다음 페이지로 이동
        }
    }

    // ✅ 사용자의 댓글 조회
    @Transactional
    public Page<CommentDTO> getUserComments(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10));
        return commentRepository.findByUserId(userId, pageable)
                .map(CommentDTO::new);
    }

    // ✅ 기존 CommentService의 deleteComment 메서드 활용
    @Transactional
    public void deleteUserComment(Long commentId, Long userId) {
        commentService.deleteComment(commentId, userId);
    }

    //  사용자 정보 조회
    @Transactional(readOnly = true)
    public UserDTO getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));
        return convertToDTO(user);
    }

    // 사용자 정보 등록
    @Transactional
    public UserDTO createUserInfo(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));

        user.setMajor(userDTO.getMajor());
        user.setGrade(userDTO.getGrade());

        userRepository.save(user);
        return convertToDTO(user);
    }

    // 사용자 정보 수정
    @Transactional
    public UserDTO updateUserInfo(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));

        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword()); // 비밀번호 변경
        }
        if (userDTO.getMajor() != null) {
            user.setMajor(userDTO.getMajor());
        }
        if (userDTO.getGrade() != null) {
            user.setGrade(userDTO.getGrade());
        }

        userRepository.save(user);
        return convertToDTO(user);
    }

    // 사용자 정보 삭제
    @Transactional
    public void deleteUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    // ✅ User → UserDTO 변환 메서드
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .major(user.getMajor())
                .grade(user.getGrade())
                .build();
    }
}




