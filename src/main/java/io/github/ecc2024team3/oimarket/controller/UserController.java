package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.dto.UserDTO;
import io.github.ecc2024team3.oimarket.service.UserService;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService; // UserService 사용\

    // 로그인 → JWT 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);
        return ResponseEntity.ok().body(token);
    }

    // 회원가입 (이메일 인증 없이 진행)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO userDTO) {
        try {
            // 회원가입 처리
            Long userId = userService.signup(userDTO);
            return ResponseEntity.ok().body(Map.of("userId", userId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 로그아웃 (POST)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = JwtTokenProvider.resolveToken(request);
        if (token != null) {
            userService.logout(token);
            return ResponseEntity.ok().body(Map.of("message", "로그아웃 성공"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "유효한 토큰이 없습니다."));
    }

    // 회원 탈퇴 (DELETE) - JWT 토큰 기반
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        String token = JwtTokenProvider.resolveToken(request);
        if (token != null) {
            userService.deleteUser(userId, token);
            return ResponseEntity.ok().body(Map.of("message", "회원 탈퇴 완료"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "유효한 토큰이 없습니다."));
    }
}


