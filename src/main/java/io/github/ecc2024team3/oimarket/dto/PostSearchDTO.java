package io.github.ecc2024team3.oimarket.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchDTO {
    private String keyword;
    private String transaction_status;
    private String location;
    private Integer min_price;
    private Integer max_price;
}
