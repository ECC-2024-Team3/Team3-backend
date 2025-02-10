package io.github.ecc2024team3.oimarket.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostUpdateDTO {
    private String title;
    private String location;
    private Integer price;
    private String transaction_status;
    private String content;
    private List<String> images;
}
