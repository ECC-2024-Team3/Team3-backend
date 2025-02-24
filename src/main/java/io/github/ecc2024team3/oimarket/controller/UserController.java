package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import io.github.ecc2024team3.oimarket.util.RedisUtil;
import io.github.ecc2024team3.oimarket.dto.UserDTO;
import io.github.ecc2024team3.oimarket.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService; // UserService 사용
    private final RedisUtil redisUtil; // 이메일 인증 검증을 위한 RedisUtil 추가
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/force-verified")
    public ResponseEntity<String> forceVerify(@RequestParam(name = "email", required = false) String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: 이메일을 입력해주세요.");
        }

        try {
            // Redis에 강제 인증 값 저장
            redisUtil.setData("email_verified:" + email, "true");

            // 저장된 값 확인을 위해 Redis에서 다시 가져옴
            String storedValue = redisUtil.getData("email_verified:" + email);
            if (!"true".equals(storedValue)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Redis 저장 실패: " + storedValue);
            }

            // DB에서도 인증 상태 업데이트
            userService.verifyUser(email);

            return ResponseEntity.ok("Email verification status set to true and updated in DB.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during force verification: " + e.getMessage());
        }
    }
    //  로그인 → JWT 토큰 발급
    @RequestMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
        }

        // 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        // 토큰 만료 시간 가져오기
        long expiration = jwtTokenProvider.getExpiration(token) - System.currentTimeMillis();
        if (expiration > 0) {
            redisUtil.addToBlacklist(token, expiration);
        }

        // SecurityContext 초기화
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    //  회원가입 (이메일 인증 검증 포함)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO userDTO) {
        //  이메일 인증 여부 확인 ( redis에 저장된 인증 코드 체크)
        String storedCode = redisUtil.getData("email_verified:" + userDTO.getEmail()); // ✅ key format 확인

        if (storedCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "이메일 인증이 필요합니다."));
        }

        try {
            //  회원가입 처리
            Long userId = userService.signup(userDTO);
            return ResponseEntity.ok().body(Map.of("userId", userId));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
        }

        // JWT 토큰 유효성 검사
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // 토큰에서 email 추출
        String email = jwtTokenProvider.getEmail(token);

        try {
            // 유저 삭제 처리
            userService.deleteUserByEmail(email);

            // 토큰 블랙리스트 추가 (로그아웃과 동일한 처리)
            long expiration = jwtTokenProvider.getExpiration(token) - System.currentTimeMillis();
            if (expiration > 0) {
                redisUtil.addToBlacklist(token, expiration);
            }

            // SecurityContext 초기화
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok("계정이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("유저 탈퇴 중 오류 발생: " + e.getMessage());
        }
    }


}

