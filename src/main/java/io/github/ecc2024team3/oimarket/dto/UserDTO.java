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
    private String password; // ✅ getPassword()를 사용하기 위해 포함
    private String nickname;
    private String profileImage; // ✅ getProfileImage()를 사용하기 위해 포함

    // Entity -> DTO 변환 생성자
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword(); // ✅ User 엔티티의 password를 포함
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}