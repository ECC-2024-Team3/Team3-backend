package io.github.ecc2024team3.oimarket.service;

import io.github.ecc2024team3.oimarket.dto.UserDTO;
import io.github.ecc2024team3.oimarket.entity.User;
import io.github.ecc2024team3.oimarket.repository.UserRepository;
import io.github.ecc2024team3.oimarket.token.JwtTokenProvider;
import io.github.ecc2024team3.oimarket.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;


    // 회원가입 (signup)
    @Transactional
    public Long signup(UserDTO userDto) {
        // `getConfirmPassword()`가 없는 경우, UserDTO에 추가해야 함
        if (userDto.getConfirmPassword() != null && !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이메일 인증 여부 확인
        String storedCode = redisUtil.getData(userDto.getEmail());
        if (storedCode == null) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }

        // 아이디 중복 체크
        Optional<User> existingUser = userRepository.findById(userDto.getUserId());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 ID입니다.");
        }

        // 이메일 중복 체크
        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 사용자 정보 저장 (userRole 제거)
        User user = User.builder()
                .userId(userDto.getUserId())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .profileImage(userDto.getProfileImage())
                .build();

        userRepository.save(user);
        redisUtil.deleteData(userDto.getEmail());
        return user.getUserId();
    }

    // 로그인 (JWT 토큰 반환)
    public String login(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // userRole 제거 → 기본적으로 "ROLE_USER" 부여
        return jwtTokenProvider.createToken(user.getEmail(), List.of("ROLE_USER"));
    }

    // 사용자 정보 조회
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return new UserDTO(user);
    }
}