package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.*;
import io.github.ecc2024team3.oimarket.entity.Bookmark;
import io.github.ecc2024team3.oimarket.entity.Like;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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


    // 게시글 조회, 삭제(선택, 전체), 수정

    // ✅ 사용자가 작성한 모든 게시글 조회
    @Transactional
    public List<Long> getUserPosts(Long userId) {
        return postRepository.findByUser(userId)
                .stream()
                .map(Post::getPostId) // 게시글 ID만 추출
                .collect(Collectors.toList());
    }

    // ✅ 사용자가 선택한 게시글만 삭제
    @Transactional
    public void deleteSelectedUserPosts(Long userId, List<Long> selectedPostIds) {
        List<Long> userPostIds = getUserPosts(userId); // 해당 사용자의 게시글 ID 조회
        selectedPostIds.stream()
                .filter(userPostIds::contains) // 사용자의 게시글인지 확인
                .forEach(postId -> postService.deletePost(postId, userId)); // 선택된 게시글 삭제
    }


    // ✅ 사용자가 작성한 모든 게시글 삭제
    @Transactional
    public void deleteAllUserPosts(Long userId) {
        List<Long> postIds = getUserPosts(userId); // 해당 사용자의 게시글 ID 조회
        postIds.forEach(postId -> postService.deletePost(postId, userId)); // 게시글 삭제
    }

    // ✅ 마이페이지에서 특정 게시글 수정하기
    @Transactional
    public PostDTO updateMyPost(Long userId, Long postId, PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(postId, userId, postUpdateDTO);
    }

    // 좋아요 목록 조회, 삭제(선택, 전체)

    // ✅ 사용자가 좋아요한 게시글 목록 조회
    @Transactional
    public List<Long>getLikedPosts(Long userId) {
        List<Like> likes = likeRepository.findByUser(userId);
        return likes
                .stream()
                .map(like -> like.getPost().getPostId()) // 게시글 ID만 추출
                .collect(Collectors.toList());
    }

    // ✅ 사용자가 선택한 게시글만 좋아요 해제
    @Transactional
    public void deleteSelectedLikedPosts(Long userId, List<Long> selectedPostIds) {
        List<Like> likes = likeRepository.findByUser(userId);
        List<Like> likesToRemove = likes.stream()
                .filter(like -> selectedPostIds.contains(like.getPost().getPostId()))
                .collect(Collectors.toList());
        likeRepository.deleteAll(likesToRemove); // 선택된 좋아요 데이터 삭제
    }

    // ✅ 사용자가 좋아요한 모든 게시글 좋아요 해제
    @Transactional
    public void deleteAllLikedPosts(Long userId) {
        List<Like> likes = likeRepository.findByUser(userId);
        likeRepository.deleteAll(likes); // 모든 좋아요 데이터 삭제
    }

    // 북마크 목록 조회, 삭제(선택, 전체)

    // ✅ 사용자가 북마크한 게시글 목록 조회
    @Transactional
    public List<Long>getBookmarkedPosts(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(userId);
        return bookmarks
                .stream()
                .map(bookmark -> bookmark.getPost().getPostId()) // 게시글 ID만 추출
                .collect(Collectors.toList());
    }

    // ✅ 사용자가 선택한 게시글만 북마크 해제
    @Transactional
    public void deleteSelectedBookmarkedPosts(Long userId, List<Long> selectedPostIds) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(userId);
        List<Bookmark> bookmarksToRemove = bookmarks.stream()
                .filter(bookmark -> selectedPostIds.contains(bookmark.getPost().getPostId()))
                .collect(Collectors.toList());
        bookmarkRepository.deleteAll(bookmarksToRemove); // 선택된 북마크 데이터 삭제
    }

    // ✅ 사용자가 북마크한 모든 게시글 북마크 해제
    @Transactional
    public void deleteAllBookmarkedPosts(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUser(userId);
        bookmarkRepository.deleteAll(bookmarks); // 모든 북마크 데이터 삭제
    }

    //댓글 조회, 삭제

    // 사용자의 댓글 조회
    @Transactional
    public List<CommentDTO> getUserComments(Long userId) {
        return commentRepository.findByUserId(userId).stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    // ✅ 기존 CommentService의 deleteComment 메서드 활용
    @Transactional
    public void deleteUserComment(Long commentId, Long userId) {
        commentService.deleteComment(commentId, userId); // ✅ CommentService 호출
    }
}
