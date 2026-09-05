package com.lensify.service;

import java.util.List;

import com.lensify.dto.category.CategoryRequestDto;
import com.lensify.dto.category.CategoryResponseDto;
import com.lensify.response.ApiResponse;

public interface CategoryService {

    ApiResponse<CategoryResponseDto> addCategory(CategoryRequestDto request);

    ApiResponse<List<CategoryResponseDto>> getAllCategories();

    ApiResponse<CategoryResponseDto> getCategoryById(Long id);

    ApiResponse<CategoryResponseDto> updateCategory(
            Long id,
            CategoryRequestDto request
    );

    ApiResponse<String> deleteCategory(Long id);
}