package io.github.ecc2024team3.oimarket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EmailDTO {

    // 이메일 주소
    private String email;
    // 인증 코드
    private String verifyCode;

    public String getMail() {
        return email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }
}

