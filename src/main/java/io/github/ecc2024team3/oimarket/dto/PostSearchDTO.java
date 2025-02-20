package io.github.ecc2024team3.oimarket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchDTO {
    private String keyword;
    private String transactionStatus;
    private String category;
    private String itemCondition;
    private String location;
    private Integer minPrice;
    private Integer maxPrice;
}