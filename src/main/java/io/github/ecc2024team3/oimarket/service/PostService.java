package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostSearchDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.TransactionStatus;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.BookmarkRepository;
import io.github.ecc2024team3.oimarket.repository.LikeRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository, BookmarkRepository bookmarkRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    // ✅ 게시글 생성 (Create)
    @Transactional
    public PostDTO createPost(PostCreateDTO postDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post(postDTO, user);
        post.setTransactionStatus(TransactionStatus.valueOf(postDTO.getTransactionStatus())); // ✅ ENUM 변환

        postRepository.save(post);
        String representativeImage = (post.getImages() != null && !post.getImages().isEmpty()) 
            ? post.getImages().get(0).getImageUrl() 
            : null;

        return new PostDTO(post, representativeImage);  // Return PostDTO with representative image
    }

    // ✅ 전체 게시글 조회 (Read - 모든 게시글)
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> {
            String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
            return new PostDTO(post, representativeImage); // Return without likes/bookmarks
        }).collect(Collectors.toList());
    }

    // ✅ 개별 게시글 조회 (Read - 특정 게시글)
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        int likesCount = likeRepository.countByPost_PostId(postId);
        int bookmarksCount = bookmarkRepository.countByPost_PostId(postId);
        boolean liked = likeRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
        boolean bookmarked = bookmarkRepository.existsByUser_UserIdAndPost_PostId(userId, postId);

        String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();

        return PostDTO.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .title(post.getTitle())
                .location(post.getLocation())
                .price(post.getPrice())
                .transactionStatus(post.getTransactionStatus().name())
                .content(post.getContent())
                .representativeImage(representativeImage)
                .likesCount(likesCount)
                .bookmarksCount(bookmarksCount)
                .liked(liked)
                .bookmarked(bookmarked)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // ✅ 게시글 수정 (Update)
    @Transactional
    public PostDTO updatePost(Long postId, Long userId, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("게시글을 수정할 권한이 없습니다.");
        }

        if (postUpdateDTO.getTransactionStatus() != null) {
            post.setTransactionStatus(TransactionStatus.valueOf(postUpdateDTO.getTransactionStatus()));  // ✅ ENUM 변환
        }

        post.setUpdatedAt(java.time.LocalDateTime.now());
        postRepository.save(post);

        String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();

        return new PostDTO(post, representativeImage);  // Return PostDTO with representative image
    }

    // ✅ 게시글 삭제 (Delete)
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // ✅ 검색 기능 추가
    @Transactional(readOnly = true)
    public List<PostDTO> searchPosts(PostSearchDTO searchDTO) {
        TransactionStatus transactionStatus = null;
        if (searchDTO.getTransactionStatus() != null) {
            transactionStatus = TransactionStatus.valueOf(searchDTO.getTransactionStatus());
        }
        
        List<Post> posts = postRepository.searchPosts(
                searchDTO.getKeyword(),
                transactionStatus,  // ✅ ENUM으로 변환 후 전달
                searchDTO.getLocation(),
                searchDTO.getMinPrice(),
                searchDTO.getMaxPrice()
        );

        return posts.stream().map(post -> {
            String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
            return new PostDTO(post, representativeImage);  // Return PostDTO with representative image
        }).collect(Collectors.toList());
    }
}
