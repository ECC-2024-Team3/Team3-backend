package io.github.ecc2024team3.oimarket.repository;

import io.github.ecc2024team3.oimarket.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT p FROM Like p WHERE p.user.id = :userId")
    List<Like> findByUser(@Param("userId") Long userId);
}
