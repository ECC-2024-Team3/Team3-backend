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
    private String nickname;
    private String profileImage;

    // Entity -> DTO 변환 생성자
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}