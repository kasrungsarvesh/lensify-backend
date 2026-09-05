package com.lensify.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lensify.dto.product.ProductRequestDto;
import com.lensify.dto.product.ProductResponseDto;
import com.lensify.entity.Category;
import com.lensify.entity.Product;
import com.lensify.exception.DuplicateResourceException;
import com.lensify.exception.ResourceNotFoundException;
import com.lensify.repository.CategoryRepository;
import com.lensify.repository.ProductRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ApiResponse<ProductResponseDto> addProduct(
            ProductRequestDto request) {

        // Check barcode
        if (request.getBarcode() != null
                && !request.getBarcode().isBlank()
                && productRepository.findByBarcode(request.getBarcode()).isPresent()) {

            throw new DuplicateResourceException(
                    "Barcode already exists."
            );
        }

        // Find category
        Category category = categoryRepository.findById(
                request.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Category does not exist."
                )
        );

        Product product = new Product();

        product.setProductName(request.getProductName());
        product.setBrand(request.getBrand());
        product.setModelNumber(request.getModelNumber());
        product.setBarcode(request.getBarcode());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setColor(request.getColor());
        product.setSize(request.getSize());
        product.setStatus(request.getStatus());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        try {

            product = productRepository.save(product);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateResourceException(
                    "Product details already exist."
            );
        }

        return new ApiResponse<>(
                true,
                "Product added successfully.",
                mapToDto(product)
        );
    }

    @Override
    public ApiResponse<List<ProductResponseDto>> getAllProducts() {

        List<ProductResponseDto> response =
                productRepository.findAll()
                        .stream()
                        .map(this::mapToDto)
                        .toList();

        return new ApiResponse<>(
                true,
                "Products fetched successfully.",
                response
        );
    }

    @Override
    public ApiResponse<ProductResponseDto> getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product does not exist."
                        )
                );

        return new ApiResponse<>(
                true,
                "Product fetched successfully.",
                mapToDto(product)
        );
    }

    @Override
    public ApiResponse<ProductResponseDto> updateProduct(
            Long id,
            ProductRequestDto request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product does not exist."
                        )
                );

        // Check barcode
        if (request.getBarcode() != null
                && !request.getBarcode().isBlank()
                && productRepository.existsByBarcodeAndProductIdNot(
                        request.getBarcode(),
                        id)) {

            throw new DuplicateResourceException(
                    "Barcode already exists."
            );
        }

        // Find category
        Category category = categoryRepository.findById(
                request.getCategoryId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Category does not exist."
                )
        );

        product.setProductName(request.getProductName());
        product.setBrand(request.getBrand());
        product.setModelNumber(request.getModelNumber());
        product.setBarcode(request.getBarcode());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setColor(request.getColor());
        product.setSize(request.getSize());
        product.setStatus(request.getStatus());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        try {

            product = productRepository.save(product);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateResourceException(
                    "Product details already exist."
            );
        }

        return new ApiResponse<>(
                true,
                "Product updated successfully.",
                mapToDto(product)
        );
    }

    @Override
    public ApiResponse<String> deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product does not exist."
                        )
                );

        productRepository.delete(product);

        return new ApiResponse<>(
                true,
                "Product deleted successfully.",
                null
        );
    }

    @Override
    public ApiResponse<List<ProductResponseDto>> searchProducts(
            String keyword) {

        if (keyword == null || keyword.isBlank()) {

            return new ApiResponse<>(
                    true,
                    "Please enter a search keyword.",
                    List.of()
            );
        }

        List<Product> products =
                productRepository.searchProducts(
                        keyword.trim()
                );

        List<ProductResponseDto> result =
                products.stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Products fetched successfully.",
                result
        );
    }

    private ProductResponseDto mapToDto(Product product) {

        ProductResponseDto dto = new ProductResponseDto();

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());

        if (product.getCategory() != null) {

            dto.setCategoryId(
                    product.getCategory().getCategoryId()
            );

            dto.setCategoryName(
                    product.getCategory().getCategoryName()
            );
        }

        dto.setBrand(product.getBrand());
        dto.setModelNumber(product.getModelNumber());
        dto.setBarcode(product.getBarcode());
        dto.setPurchasePrice(product.getPurchasePrice());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setColor(product.getColor());
        dto.setSize(product.getSize());
        dto.setStatus(product.getStatus());
        dto.setDescription(product.getDescription());

        return dto;
    }
}