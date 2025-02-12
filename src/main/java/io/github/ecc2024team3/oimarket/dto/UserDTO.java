// 대체 예정 (다른 담당자)

package io.github.ecc2024team3.oimarket.dto;

import io.github.ecc2024team3.oimarket.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long user_id;
    private String email;
    private String password; // ✅ getPassword()를 사용하기 위해 포함
    private String nickname;
    private String profile_image; // ✅ getProfile_image()를 사용하기 위해 포함

    // Entity -> DTO 변환 생성자
    public UserDTO(User user) {
        this.user_id = user.getUser_id();
        this.email = user.getEmail();
        this.password = user.getPassword(); // ✅ User 엔티티의 password를 포함
        this.nickname = user.getNickname();
        this.profile_image = user.getProfile_image();
    }
}
