package com.lensify.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.lensify.dto.customer.CustomerRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {

    private Long orderItemId;

    private Long lensId;

    private String lensBrand;

    private Integer quantity;

    private BigDecimal price;

}
