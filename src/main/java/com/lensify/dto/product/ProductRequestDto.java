package com.lensify.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Category is required")
    private Long categoryId;

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