package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUser_UserIdAndPost_PostId(Long userId, Long postId); // 특정 유저가 특정 게시글에 좋아요 눌렀는지 확인
    int countByPost_PostId(Long postId); // 특정 게시글의 좋아요 개수 조회
    boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId); // 특정 유저가 특정 게시글에 좋아요 눌렀는지 여부 확인
}