package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String email;
    private String password;
    private String confirmPassword; // 추가
    private String nickname;
    private String profileImage;
    private String major;  // 전공
    private String grade;  // 학년

    // Entity -> DTO 변환 생성자
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.major = user.getMajor();
        this.grade = user.getGrade();
    }
}
