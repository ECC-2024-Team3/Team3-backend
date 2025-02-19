package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // ✅ 특정 사용자의 댓글 조회 (페이징 추가)
    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId")
    Page<Comment> findByUserId(@Param("userId") Long userId, Pageable pageable);

    // ✅ 특정 게시글 기준으로 댓글 조회 (페이징 추가)
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
    Page<Comment> findByPostId(@Param("postId") Long postId, Pageable pageable);
}