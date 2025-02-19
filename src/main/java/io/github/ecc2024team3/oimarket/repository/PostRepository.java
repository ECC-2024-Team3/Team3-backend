package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Category;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
            "AND (:transactionStatus IS NULL OR p.transactionStatus = :transactionStatus) " +
            "AND (:category IS NULL OR p.category = :category) " +  
            "AND (:location IS NULL OR p.location LIKE %:location%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) ")
    Page<Post> searchPosts(
            @Param("keyword") String keyword,
            @Param("transactionStatus") TransactionStatus transactionStatus,
            @Param("category") Category category,
            @Param("location") String location,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            Pageable pageable
    );
    
    @Query("SELECT p FROM Post p WHERE p.user.userId = :userId")
    Page<Post> findByUser(@Param("userId") Long userId, Pageable pageable);
}