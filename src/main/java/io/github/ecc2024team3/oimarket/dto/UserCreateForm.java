package io.github.ecc2024team3.oimarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class UserCreateForm {

    @NotEmpty(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5~20자 사이여야 합니다.")
    private String id;

    @NotEmpty(message = "사용자 이름은 필수 입력 값입니다.")
    @Size(min = 3, max = 25, message = "사용자 이름은 3~25자 사이여야 합니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식을 입력해야 합니다.")
    private String email;
}
