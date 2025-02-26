package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.UserDTO;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private final ConcurrentHashMap<String, Boolean> tokenBlacklist = new ConcurrentHashMap<>();


    @Transactional
    public Long signup(UserDTO userDto) {
        // 비밀번호 확인 로직 수정 (암호화하기 전에 비교)
        if (userDto.getConfirmPassword() != null
                && !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 닉네임 중복 체크
        Optional<User> existingNickname = userRepository.findByNickname(userDto.getNickname());
        if (existingNickname.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }

        // 이메일 중복 체크
        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 사용자 정보 저장 (여기서 비밀번호를 암호화)
        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))  // 암호화
                .nickname(userDto.getNickname())
                .profileImage(userDto.getProfileImage())
                .build();

        userRepository.save(user);
        return user.getUserId();
    }

    public Map<String, Object> login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
    
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    
        String token = jwtTokenProvider.createToken(user.getEmail(), List.of("ROLE_USER"));
    
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getUserId());
        
        return response;
    }
    

    //  로그아웃 (토큰 블랙리스트에 추가)
    public void logout(String token) {
        tokenBlacklist.put(token, true);
    }

    //  회원 탈퇴 (JWT 토큰 기반)
    @Transactional
    public void deleteUser(Long userId, String token) {
        if (tokenBlacklist.containsKey(token)) {
            throw new IllegalStateException("이미 로그아웃된 사용자입니다.");
        }

        String email = jwtTokenProvider.getEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        if (!user.getUserId().equals(userId)) {
            throw new IllegalStateException("권한이 없습니다.");
        }

        userRepository.delete(user);
        tokenBlacklist.put(token, true); // 회원 탈퇴 후 해당 토큰 무효화
    }




}

