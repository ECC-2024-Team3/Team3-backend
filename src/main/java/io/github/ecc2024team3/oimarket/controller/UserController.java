package io.github.ecc2024team3.oimarket.controller;

import io.github.ecc2024team3.oimarket.util.RedisUtil;
import io.github.ecc2024team3.oimarket.dto.UserDTO;
import io.github.ecc2024team3.oimarket.service.UserService;
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
    private final UserService userService; // UserService 사용
    private final RedisUtil redisUtil; // 이메일 인증 검증을 위한 RedisUtil 추가

    //  로그인 → JWT 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);
        return ResponseEntity.ok().body(token);
    }

    //  회원가입 (이메일 인증 검증 포함)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserDTO userDTO) {
        //  이메일 인증 여부 확인 ( redis에 저장된 인증 코드 체크)
        String storedCode = redisUtil.getData(userDTO.getEmail());
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


}

