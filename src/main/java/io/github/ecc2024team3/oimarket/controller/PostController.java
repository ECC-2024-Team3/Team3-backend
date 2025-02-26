package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.PostDTO;
import io.github.ecc2024team3.oimarket.dto.PostSearchDTO;
import io.github.ecc2024team3.oimarket.dto.PostCreateDTO;
import io.github.ecc2024team3.oimarket.dto.PostUpdateDTO;
import io.github.ecc2024team3.oimarket.service.PostService;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    public PostController(PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ✅ 게시글 생성 (POST /api/posts)
    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody @Valid PostCreateDTO postCreateDTO,
                                              @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(postService.createPost(postCreateDTO, userId));
    }


    // ✅ 전체 게시글 조회 (로그인한 사용자 ID 포함(좋아요/북마크 여부 확인을 위함))
    @GetMapping
    public ResponseEntity<Page<PostDTO>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,  // 기본 페이지 0
            @RequestParam(defaultValue = "10") int size) { // 기본 size 10
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }

    // ✅ 개별 게시글 조회 (GET /api/posts/{postId})
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId,
                                               @RequestParam(required = false) Long userId) {  // ✅ `required = false` 추가
        return ResponseEntity.ok(postService.getPostById(postId, userId));
    }    

    // ✅ 게시글 수정 (PATCH /api/posts/{postId})
    @PatchMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long postId,
                                              @RequestHeader("Authorization") String token,
                                              @RequestBody PostUpdateDTO postUpdateDTO) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        return ResponseEntity.ok(postService.updatePost(postId, userId, postUpdateDTO));
    }

    // ✅ 게시글 삭제 (DELETE /api/posts/{postId})
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           @RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // ✅ 검색 기능
    @GetMapping("/search")
    public ResponseEntity<Page<PostDTO>> searchPosts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String transactionStatus,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PostSearchDTO searchDTO = PostSearchDTO.builder()
                .keyword(keyword)
                .transactionStatus(transactionStatus)
                .location(location)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .build();

        return ResponseEntity.ok(postService.searchPosts(searchDTO, page, size));
    }
}