package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p " +
            "WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
            "AND (:transaction_status IS NULL OR p.transaction_status = :transaction_status) " +
            "AND (:location IS NULL OR p.location LIKE %:location%) " +
            "AND (:min_price IS NULL OR p.price >= :min_price) " +
            "AND (:max_price IS NULL OR p.price <= :max_price) ")
    List<Post> searchPosts(@Param("keyword") String keyword,
                           @Param("transaction_status") String transaction_status,
                           @Param("location") String location,
                           @Param("min_price") Integer min_price,
                           @Param("max_price") Integer max_price);
}

