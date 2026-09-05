package com.lensify.dto.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long orderItemId;

    private Long productId;
    private String productName;

    private Long lensId;
    private String lensBrand;

    private Integer quantity;

    private BigDecimal price;
}