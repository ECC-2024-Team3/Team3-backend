package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostSearchDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.entity.Post;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.PostRepository;
import io.github.ecc2024team3.oimarket.repository.LikeRepository;
import io.github.ecc2024team3.oimarket.repository.BookmarkRepository;
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
    public PostDTO createPost(PostCreateDTO postDTO, Long user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post(postDTO, user);
        postRepository.save(post);

        return new PostDTO(post);
    }

    // ✅ 전체 게시글 조회 (Read - 모든 게시글)
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImage_url();

            return PostDTO.builder()
                    .post_id(post.getPost_id())
                    .user_id(post.getUser().getUser_id())
                    .title(post.getTitle())
                    .location(post.getLocation())
                    .price(post.getPrice())
                    .transaction_status(post.getTransaction_status())
                    .content(post.getContent())
                    .representative_image(representativeImage)
                    .likes_count(likeRepository.countByPost_PostId(post.getPost_id()))
                    .bookmarks_count(bookmarkRepository.countByPost_PostId(post.getPost_id()))
                    .created_at(post.getCreated_at())
                    .updated_at(post.getUpdated_at())
                    .build();
        }).collect(Collectors.toList());
    }

    // ✅ 개별 게시글 조회 (Read - 특정 게시글)
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long post_id, Long user_id) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        int likesCount = likeRepository.countByPost_PostId(post_id);
        int bookmarksCount = bookmarkRepository.countByPost_PostId(post_id);
        boolean liked = likeRepository.existsByUser_UserIdAndPost_PostId(user_id, post_id);
        boolean bookmarked = bookmarkRepository.existsByUser_UserIdAndPost_PostId(user_id, post_id);

        String representativeImage = post.getImages().isEmpty() ? null : post.getImages().get(0).getImage_url();

        return PostDTO.builder()
                .post_id(post.getPost_id())
                .user_id(post.getUser().getUser_id())
                .title(post.getTitle())
                .location(post.getLocation())
                .price(post.getPrice())
                .transaction_status(post.getTransaction_status())
                .content(post.getContent())
                .representative_image(representativeImage)
                .likes_count(likesCount)
                .bookmarks_count(bookmarksCount)
                .liked(liked)
                .bookmarked(bookmarked)
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .build();
    }

    // ✅ 게시글 수정 (Update)
    @Transactional
    public PostDTO updatePost(Long post_id, Long user_id, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUser_id().equals(user_id)) {
            throw new RuntimeException("게시글을 수정할 권한이 없습니다.");
        }

        if (postUpdateDTO.getTitle() != null) post.setTitle(postUpdateDTO.getTitle());
        if (postUpdateDTO.getLocation() != null) post.setLocation(postUpdateDTO.getLocation());
        if (postUpdateDTO.getPrice() != null) post.setPrice(postUpdateDTO.getPrice());
        if (postUpdateDTO.getTransaction_status() != null) post.setTransaction_status(postUpdateDTO.getTransaction_status());
        if (postUpdateDTO.getContent() != null) post.setContent(postUpdateDTO.getContent());

        post.setUpdated_at(java.time.LocalDateTime.now());
        postRepository.save(post);

        return new PostDTO(post);
    }

    // ✅ 게시글 삭제 (Delete)
    @Transactional
    public void deletePost(Long post_id, Long user_id) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getUser_id().equals(user_id)) {
            throw new RuntimeException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // ✅ 검색 기능 추가
    @Transactional(readOnly = true)
    public List<PostDTO> searchPosts(PostSearchDTO searchDTO) {
        List<Post> posts = postRepository.searchPosts(
                searchDTO.getKeyword(),
                searchDTO.getTransaction_status(),
                searchDTO.getLocation(),
                searchDTO.getMin_price(),
                searchDTO.getMax_price()
        );

        return posts.stream().map(post -> PostDTO.builder()
                .post_id(post.getPost_id())
                .user_id(post.getUser().getUser_id())
                .title(post.getTitle())
                .location(post.getLocation())
                .price(post.getPrice())
                .transaction_status(post.getTransaction_status())
                .content(post.getContent())
                .likes_count(0)  // 기본값 설정, 실제 값은 좋아요 기능에서 따로 가져옴
                .bookmarks_count(0)  // 기본값 설정
                .created_at(post.getCreated_at())
                .updated_at(post.getUpdated_at())
                .build()).collect(Collectors.toList());
    }
}