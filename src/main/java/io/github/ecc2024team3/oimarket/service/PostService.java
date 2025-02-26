package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostSearchDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.entity.Category;
import io.github.ecc2024team3.oimarket.entity.ItemCondition;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.TransactionStatus;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.BookmarkRepository;
import io.github.ecc2024team3.oimarket.repository.LikeRepository;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.UserRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        try {
            System.out.println("📌 createPost 호출됨. userId: " + userId);
            System.out.println("📌 전달받은 데이터: " + postDTO.toString());

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("🚨 사용자를 찾을 수 없습니다. userId: " + userId));

            if (postDTO.getTitle() == null || postDTO.getTitle().isBlank()) {
                throw new IllegalArgumentException("🚨 제목을 입력해주세요.");
            }
            if (postDTO.getLocation() == null || postDTO.getLocation().isBlank()) {
                throw new IllegalArgumentException("🚨 장소를 입력해주세요.");
            }
            if (postDTO.getPrice() == null) {
                throw new IllegalArgumentException("🚨 가격을 입력해주세요.");
            }
            if (postDTO.getCategory() == null) {
                throw new IllegalArgumentException("🚨 카테고리를 선택해주세요.");
            }
            if (postDTO.getItemCondition() == null) {
                throw new IllegalArgumentException("🚨 제품상태를 선택해주세요.");
            }

            // ✅ ENUM 변환 시도
            TransactionStatus status = TransactionStatus.valueOf(postDTO.getTransactionStatus());
            Category category = Category.valueOf(postDTO.getCategory());
            ItemCondition itemCondition = ItemCondition.valueOf(postDTO.getItemCondition());

            System.out.println("📌 ENUM 변환 성공: " + status + ", " + category + ", " + itemCondition);

            Post post = new Post(postDTO, user);
            post.setTransactionStatus(status);
            post.setCategory(category);
            post.setItemCondition(itemCondition);

            postRepository.save(post);
            
            String representativeImage = (post.getImages() != null && !post.getImages().isEmpty()) 
                ? post.getImages().get(0).getImageUrl() 
                : null;

            return new PostDTO(post, representativeImage);
        } catch (Exception e) {
            System.err.println("🚨 게시글 생성 중 오류 발생: " + e.getMessage());
            e.printStackTrace(); // ✅ 로그 출력
            throw e;
        }
    }



    // ✅ 전체 게시글 조회 (Read - 모든 게시글)
    @Transactional(readOnly = true)
    public Page<PostDTO> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 기본값 설정
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> {
            String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
            return new PostDTO(post, representativeImage);
        });
    }

    // ✅ 개별 게시글 조회 (Read - 특정 게시글)
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    
        int likesCount = likeRepository.countByPost_PostId(postId);
        int bookmarksCount = bookmarkRepository.countByPost_PostId(postId);
        
        // ✅ userId가 null이면 기본값(false) 설정
        boolean liked = (userId != null) && likeRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
        boolean bookmarked = (userId != null) && bookmarkRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
    
        String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
    
        return PostDTO.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .title(post.getTitle())
                .location(post.getLocation())
                .price(post.getPrice())
                .transactionStatus(post.getTransactionStatus().name())
                .category(post.getCategory().name())
                .itemCondition(post.getItemCondition().name())
                .content(post.getContent())
                .representativeImage(representativeImage)
                .likesCount(likesCount)
                .bookmarksCount(bookmarksCount)
                .liked(liked)   // ✅ userId가 없으면 기본 false 반환
                .bookmarked(bookmarked)  // ✅ userId가 없으면 기본 false 반환
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
    
        if (postUpdateDTO.getTitle() == null || postUpdateDTO.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (postUpdateDTO.getLocation() == null || postUpdateDTO.getLocation().isBlank()) {
            throw new IllegalArgumentException("장소를 입력해주세요.");
        }
        if (postUpdateDTO.getPrice() == null) {
            throw new IllegalArgumentException("가격을 입력해주세요.");
        }
        if (postUpdateDTO.getCategory() == null) {
            throw new IllegalArgumentException("카테고리를 선택해주세요.");
        }
        if (postUpdateDTO.getItemCondition() == null) {
            throw new IllegalArgumentException("제품상태를 선택해주세요.");
        }
    
        post.setTitle(postUpdateDTO.getTitle());
        post.setLocation(postUpdateDTO.getLocation());
        post.setPrice(postUpdateDTO.getPrice());
        post.setCategory(Category.valueOf(postUpdateDTO.getCategory()));
        post.setItemCondition(ItemCondition.valueOf(postUpdateDTO.getItemCondition()));
    
        if (postUpdateDTO.getTransactionStatus() != null) {
            post.setTransactionStatus(TransactionStatus.valueOf(postUpdateDTO.getTransactionStatus()));
        }
    
        post.setUpdatedAt(java.time.LocalDateTime.now());
        postRepository.save(post);
    
        String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
    
        return new PostDTO(post, representativeImage);
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
    public Page<PostDTO> searchPosts(PostSearchDTO searchDTO, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        TransactionStatus transactionStatus = null;
        Category category = null;
        ItemCondition itemCondition = null;
        
        if (searchDTO.getTransactionStatus() != null) {
            transactionStatus = TransactionStatus.valueOf(searchDTO.getTransactionStatus());
        }

        if (searchDTO.getCategory() != null) {
            category = Category.valueOf(searchDTO.getCategory());
        }

        if (searchDTO.getItemCondition() != null) {
            itemCondition = ItemCondition.valueOf(searchDTO.getItemCondition());
        }

        Page<Post> posts = postRepository.searchPosts(
                searchDTO.getKeyword(),
                transactionStatus,
                category,
                itemCondition,
                searchDTO.getLocation(),
                searchDTO.getMinPrice(),
                searchDTO.getMaxPrice(),
                pageable
        );

        return posts.map(post -> {
            String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl();
            return new PostDTO(post, representativeImage);
        });
    }
}
