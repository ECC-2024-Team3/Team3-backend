package io.github.ecc2024team3.oimarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UpdateCommentDTO {
    @NotBlank
    private String content;
}
