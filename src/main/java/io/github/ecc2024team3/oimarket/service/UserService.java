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
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 입력 값입니다.");
        }

        if (userDto.getConfirmPassword() != null && !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이메일 인증 여부 확인
        String storedCode = redisUtil.getData(userDto.getEmail());
        if (storedCode == null) {
            throw new IllegalArgumentException("이메일 인증이 필요합니다.");
        }


        // 이메일 중복 체크
        Optional<User> existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // 사용자 정보 저장 (userRole 제거)
        User user = User.builder()
                .email(userDto.getEmail())
                .password(encodedPassword) //암호화된 비밀번호를 저장
                .nickname(userDto.getNickname())
                .profileImage(userDto.getProfileImage())
                .build();

        user = userRepository.save(user); // 저장 후 반환값을 다시 할당
        redisUtil.deleteData(userDto.getEmail());
        return user.getUserId();
    }

    // 로그인 (JWT 토큰 반환)
    @Transactional(readOnly = true)
    public String login(UserDTO userDTO) {
        System.out.println("로그인 시도 이메일: " + userDTO.getEmail());

        User user = userRepository.findByEmail(userDTO.getEmail().trim())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        System.out.println("회원가입 이메일: " + user.getEmail());
        System.out.println("DB에서 찾은 사용자 이메일: " + user.getEmail());

        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // userRole 제거 → 기본적으로 "ROLE_USER" 부여
        return jwtTokenProvider.createToken(user.getEmail(), List.of("ROLE_USER"));
    }

}
