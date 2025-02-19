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
    @NotBlank(message = "제목을 입력해주세요.") 
    private String title;

    @NotBlank(message = "장소를 입력해주세요.")
    private String location;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotBlank(message = "카테고리를 선택해주세요.") 
    private String category;

    @NotBlank(message = "제품상태를 선택해주세요.")  
    private String condition;

    private String transactionStatus;
    private String content;
    private List<String> images;
}