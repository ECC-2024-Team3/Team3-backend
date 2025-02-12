package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.entity.Like;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.LikeRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public boolean toggleLike(Long user_id, Long post_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    
        return likeRepository.findByUser_UserIdAndPost_PostId(user_id, post_id)
                .map(like -> {
                    likeRepository.delete(like);
                    return false; // ✅ 좋아요 취소됨
                })
                .orElseGet(() -> {
                    likeRepository.save(new Like(user, post));
                    return true; // ✅ 좋아요 추가됨
                });
    }    
}
