package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Like;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p " +
            "WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
            "AND (:transactionStatus IS NULL OR p.transactionStatus = :transactionStatus) " +
            "AND (:location IS NULL OR p.location LIKE %:location%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) ")
    List<Post> searchPosts(
            @Param("keyword") String keyword,
            @Param("transactionStatus") TransactionStatus transactionStatus,  // ✅ ENUM 사용
            @Param("location") String location,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
    );

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId")
    List<Post> findByUser(@Param("userId") Long userId);  // 특정 사용자의 게시물 목록 조회

}
