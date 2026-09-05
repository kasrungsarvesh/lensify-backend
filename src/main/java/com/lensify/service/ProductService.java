package com.lensify.service;

import java.util.List;

import com.lensify.dto.product.ProductRequestDto;
import com.lensify.dto.product.ProductResponseDto;
import com.lensify.response.ApiResponse;

public interface ProductService {

    ApiResponse<ProductResponseDto> addProduct(ProductRequestDto request);

    ApiResponse<List<ProductResponseDto>> getAllProducts();

    ApiResponse<ProductResponseDto> getProductById(Long id);

    ApiResponse<ProductResponseDto> updateProduct(
            Long id,
            ProductRequestDto request
    );

    ApiResponse<String> deleteProduct(Long id);

    ApiResponse<List<ProductResponseDto>> searchProducts(String keyword);
}