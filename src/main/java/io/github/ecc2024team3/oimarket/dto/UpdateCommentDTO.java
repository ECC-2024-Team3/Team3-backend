package io.github.ecc2024team3.oimarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentDTO {
    @NotBlank
    private String content;
}
