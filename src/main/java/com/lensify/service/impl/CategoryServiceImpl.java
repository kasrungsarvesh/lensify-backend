package com.lensify.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lensify.dto.category.CategoryRequestDto;
import com.lensify.dto.category.CategoryResponseDto;
import com.lensify.entity.Category;
import com.lensify.exception.DuplicateResourceException;
import com.lensify.exception.ResourceNotFoundException;
import com.lensify.repository.CategoryRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ApiResponse<CategoryResponseDto> addCategory(CategoryRequestDto request) {

        // Check duplicate category name
        if (categoryRepository.findByCategoryName(request.getCategoryName()).isPresent()) {
            throw new DuplicateResourceException(
                    "Category name already exists."
            );
        }

        Category category = new Category();

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus());

        category = categoryRepository.save(category);

        CategoryResponseDto response = mapToDto(category);

        return new ApiResponse<>(
                true,
                "Category added successfully.",
                response
        );
    }

    @Override
    public ApiResponse<List<CategoryResponseDto>> getAllCategories() {

        List<CategoryResponseDto> response = categoryRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(
                true,
                "Categories fetched successfully.",
                response
        );
    }

    @Override
    public ApiResponse<CategoryResponseDto> getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category does not exist."
                        )
                );

        return new ApiResponse<>(
                true,
                "Category fetched successfully.",
                mapToDto(category)
        );
    }

    @Override
    public ApiResponse<CategoryResponseDto> updateCategory(
            Long id,
            CategoryRequestDto request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category does not exist."
                        )
                );

        // Check duplicate category name
        Optional<Category> existingCategory =
                categoryRepository.findByCategoryName(
                        request.getCategoryName()
                );

        if (existingCategory.isPresent()
                && !existingCategory.get().getCategoryId().equals(id)) {

            throw new DuplicateResourceException(
                    "Category name already exists."
            );
        }

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus());

        try {

            category = categoryRepository.save(category);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateResourceException(
                    "Category details already exist."
            );
        }

        return new ApiResponse<>(
                true,
                "Category updated successfully.",
                mapToDto(category)
        );
    }

    @Override
    public ApiResponse<String> deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category does not exist."
                        )
                );

        categoryRepository.delete(category);

        return new ApiResponse<>(
                true,
                "Category deleted successfully.",
                null
        );
    }

    /**
     * Convert Category Entity to CategoryResponseDto
     */
    private CategoryResponseDto mapToDto(Category category) {

        CategoryResponseDto dto = new CategoryResponseDto();

        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        dto.setDescription(category.getDescription());
        dto.setStatus(category.getStatus());

        return dto;
    }
}