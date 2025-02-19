package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.*;
import io.github.ecc2024team3.oimarket.entity.Bookmark;
import io.github.ecc2024team3.oimarket.entity.Like;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CommentRepository commentRepository;

    private final PostService postService;
    private final CommentService commentService;


    // ✅ 사용자가 작성한 모든 게시글 조회
    @Transactional
    public Page<Long> getUserPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10));
        return postRepository.findByUser(userId, pageable)
                .map(Post::getPostId); // 게시글 ID만 추출하여 Page<Long> 반환
    }

    // ✅ 사용자가 선택한 게시글만 삭제
    @Transactional
    public void deleteSelectedUserPosts(Long userId, List<Long> selectedPostIds) {
        List<Long> userPostIds = getUserPosts(userId, 0, Integer.MAX_VALUE).getContent(); // 전체 게시글 가져옴
        selectedPostIds.stream()
                .filter(userPostIds::contains) // 사용자의 게시글인지 확인
                .forEach(postId -> postService.deletePost(postId, userId)); // 선택된 게시글 삭제
    }

    // ✅ 사용자가 작성한 모든 게시글 삭제
    @Transactional
    public void deleteAllUserPosts(Long userId) {
        List<Long> postIds = getUserPosts(userId, 0, Integer.MAX_VALUE).getContent(); // 전체 게시글 가져옴
        postIds.forEach(postId -> postService.deletePost(postId, userId)); // 게시글 삭제
    }

    // ✅ 마이페이지에서 특정 게시글 수정하기
    @Transactional
    public PostDTO updateMyPost(Long userId, Long postId, PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(postId, userId, postUpdateDTO);
    }

    // ✅ 사용자가 좋아요한 게시글 목록 조회
    @Transactional
    public Page<Long> getLikedPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10)); // 기본 size=10
        return likeRepository.findByUserId(userId, pageable)
                .map(like -> like.getPost().getPostId()); // ✅ Like → Post ID로 변환
    }

    // ✅ 사용자가 선택한 게시글만 좋아요 해제
    @Transactional
    public void deleteSelectedLikedPosts(Long userId, List<Long> selectedPostIds) {
        List<Like> likesToRemove = likeRepository.findByUserId(userId, Pageable.unpaged()).stream()
                .filter(like -> selectedPostIds.contains(like.getPost().getPostId()))
                .collect(Collectors.toList());
        likeRepository.deleteAll(likesToRemove);
    }

    // ✅ 사용자가 좋아요한 모든 게시글 좋아요 해제
    @Transactional
    public void deleteAllLikedPosts(Long userId) {
        List<Like> likes = likeRepository.findByUserId(userId, Pageable.unpaged()).getContent();
        likeRepository.deleteAll(likes);
    }

    // ✅ 사용자가 북마크한 게시글 목록 조회
    @Transactional
    public Page<Long> getBookmarkedPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10)); // 기본 size=10
        return bookmarkRepository.findByUserId(userId, pageable)
                .map(bookmark -> bookmark.getPost().getPostId()); // ✅ Bookmark → Post ID로 변환
    }

    // ✅ 사용자가 선택한 게시글만 북마크 해제
    @Transactional
    public void deleteSelectedBookmarkedPosts(Long userId, List<Long> selectedPostIds) {
        List<Bookmark> bookmarksToRemove = bookmarkRepository.findByUserId(userId, Pageable.unpaged()).stream()
                .filter(bookmark -> selectedPostIds.contains(bookmark.getPost().getPostId()))
                .collect(Collectors.toList());
        bookmarkRepository.deleteAll(bookmarksToRemove);
    }

    // ✅ 사용자가 북마크한 모든 게시글 북마크 해제
    @Transactional
    public void deleteAllBookmarkedPosts(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId, Pageable.unpaged()).getContent();
        bookmarkRepository.deleteAll(bookmarks);
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
}
