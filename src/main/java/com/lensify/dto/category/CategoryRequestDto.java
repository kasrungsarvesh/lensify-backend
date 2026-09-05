package com.lensify.dto.category;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String description;

    private String status;
}