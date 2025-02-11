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
                                              @RequestParam Long user_id) {
        return ResponseEntity.ok(postService.createPost(postCreateDTO, user_id));
    }


    // ✅ 전체 게시글 조회 (로그인한 사용자 ID 포함(좋아요/북마크 여부 확인을 위함))
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam Long user_id) {
        return ResponseEntity.ok(postService.getAllPosts(user_id));
    }

    // ✅ 개별 게시글 조회 (GET /api/posts/{post_id})
    @GetMapping("/{post_id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long post_id,
                                               @RequestParam Long user_id) {
        return ResponseEntity.ok(postService.getPostById(post_id, user_id));
    }

    // ✅ 게시글 수정 (PATCH /api/posts/{post_id})
    @PatchMapping("/{post_id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long post_id,
                                              @RequestParam Long user_id,
                                              @RequestBody PostUpdateDTO postUpdateDTO) {
        return ResponseEntity.ok(postService.updatePost(post_id, user_id, postUpdateDTO));
    }

    // ✅ 게시글 삭제 (DELETE /api/posts/{post_id})
    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long post_id,
                                           @RequestParam Long user_id) {
        postService.deletePost(post_id, user_id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 검색 기능 (로그인한 사용자 ID 포함(좋아요/북마크 여부 확인을 위함))
    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(
            @RequestParam Long user_id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String transaction_status,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer min_price,
            @RequestParam(required = false) Integer max_price) {

        PostSearchDTO searchDTO = PostSearchDTO.builder()
                .keyword(keyword)
                .transaction_status(transaction_status)
                .location(location)
                .min_price(min_price)
                .max_price(max_price)
                .build();

        return ResponseEntity.ok(postService.searchPosts(searchDTO, user_id));
    }
}
