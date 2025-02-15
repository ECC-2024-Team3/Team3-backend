package io.github.ecc2024team3.oimarket.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDTO {
    private Long userId;
    private String title;
    private String location;
    private Integer price;
    private String transactionStatus;
    private String content;
    private List<String> images; // 업로드할 이미지 URL 리스트
}