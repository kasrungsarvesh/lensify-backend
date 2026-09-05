package com.lensify.dto.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private Long productId;

    private String productName;

    private Long categoryId;

    private String categoryName;

    private String brand;

    private String modelNumber;

    private String barcode;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private Integer stockQuantity;

    private String color;

    private String size;

    private String status;

    private String description;
}