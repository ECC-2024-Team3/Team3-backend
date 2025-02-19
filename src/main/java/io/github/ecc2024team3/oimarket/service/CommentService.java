package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.CommentDTO;
import io.github.ecc2024team3.oimarket.dto.CreateCommentDTO;
import io.github.ecc2024team3.oimarket.dto.UpdateCommentDTO;
import io.github.ecc2024team3.oimarket.entity.Comment;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    // 현재 로그인한 사용자의 ID 가져오기
    public Long getCurrentUserId(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return user.getUserId();
    }

    // 특정 게시글 기준으로 댓글 조회하기
    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByPost(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.max(size, 10)); // 기본 size=10
        return commentRepository.findByPostId(postId, pageable)
                .map(CommentDTO::new);
    }

    // 댓글 작성하기
    @Transactional
    public CommentDTO createComment(Long userId, Long postId, CreateCommentDTO dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(dto.getContent())
                .build();

        commentRepository.save(comment);
        return new CommentDTO(comment);
    }

    // 댓글 수정하기
    @Transactional
    public CommentDTO updateComment(Long userId, Long commentId, UpdateCommentDTO dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("댓글 작성자만 수정할 수 있습니다.");
        }

        comment.updateContent(dto.getContent());
        return new CommentDTO(comment);
    }

    // 댓글 삭제하기
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalStateException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}