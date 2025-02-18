package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Bookmark;
import io.github.ecc2024team3.oimarket.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUser_UserIdAndPost_PostId(Long userId, Long postId); // 특정 유저가 특정 게시글을 북마크했는지 확인
    int countByPost_PostId(Long postId); // 게시글의 북마크 개수 조회
    boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId); // 특정 유저가 특정 게시글을 북마크했는지 여부 확인

    @Query("SELECT p FROM Bookmark p WHERE p.user.id = :userId")
    List<Bookmark> findByUser(@Param("userId") Long userId);  //특정 사용자의 북마크 목록 조회

}
