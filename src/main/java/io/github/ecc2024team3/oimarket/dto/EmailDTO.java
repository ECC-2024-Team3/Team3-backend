package io.github.ecc2024team3.oimarket.dto;

import lombok.Data;

@Data // @Getter, @Setter 포함
public class EmailDTO {
    private String email;
    private String verifyCode;

    public String getMail() { return email; }
}
