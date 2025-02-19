package io.github.ecc2024team3.oimarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateDTO {
    @NotBlank(message = "Title은 비워둘 수 없습니다.")
    private String title;

    @NotBlank(message = "Category는 비워둘 수 없습니다.")
    private String category;

    @NotBlank(message = "Location은 비워둘 수 없습니다.")
    private String location;

    @NotNull(message = "Price는 null이 될 수 없습니다.")
    private Integer price;

    private String transactionStatus;
    private String content;
    private List<String> images;
}