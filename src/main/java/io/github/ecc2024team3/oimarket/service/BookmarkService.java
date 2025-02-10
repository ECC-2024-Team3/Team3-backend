package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.entity.Bookmark;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.BookmarkRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository, PostRepository postRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public boolean toggleBookmark(Long user_id, Long post_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    
        return bookmarkRepository.findByUser_UserIdAndPost_PostId(user_id, post_id)
                .map(bookmark -> {
                    bookmarkRepository.delete(bookmark);
                    return false; // ✅ 북마크 취소됨
                })
                .orElseGet(() -> {
                    bookmarkRepository.save(new Bookmark(user, post));
                    return true; // ✅ 북마크 추가됨
                });
    }    
}