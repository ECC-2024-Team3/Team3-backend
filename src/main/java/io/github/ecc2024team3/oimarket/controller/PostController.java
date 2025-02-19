package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostSearchDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // ✅ 게시글 생성 (POST /api/posts)
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid PostCreateDTO postCreateDTO,
                                              @RequestParam Long userId) {
        return ResponseEntity.ok(postService.createPost(postCreateDTO, userId));
    }

    // ✅ 전체 게시글 조회 (로그인한 사용자 ID 포함(좋아요/북마크 여부 확인을 위함))
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // ✅ 개별 게시글 조회 (GET /api/posts/{postId})
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId,
                                               @RequestParam Long userId) {
        return ResponseEntity.ok(postService.getPostById(postId, userId));
    }

    // ✅ 게시글 수정 (PATCH /api/posts/{postId})
    @PatchMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long postId,
                                              @RequestParam Long userId,
                                              @RequestBody PostUpdateDTO postUpdateDTO) {
        return ResponseEntity.ok(postService.updatePost(postId, userId, postUpdateDTO));
    }

    // ✅ 게시글 삭제 (DELETE /api/posts/{postId})
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @RequestParam Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // ✅ 검색 기능 (로그인한 사용자 ID 포함(좋아요/북마크 여부 확인을 위함))
    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String transactionStatus,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice) {

        PostSearchDTO searchDTO = PostSearchDTO.builder()
                .keyword(keyword)
                .transactionStatus(transactionStatus)
                .location(location)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        return ResponseEntity.ok(postService.searchPosts(searchDTO));
    }
}