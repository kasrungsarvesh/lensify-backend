package com.lensify.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lensify.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    boolean existsByBarcodeAndProductIdNot(String barcode, Long productId);

    @Query("""
        SELECT p FROM Product p
        WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.modelNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY p.productName ASC
    """)
    List<Product> searchProducts(@Param("keyword") String keyword);

    long countByStockQuantityGreaterThan(Integer quantity);

    long countByStockQuantityBetween(Integer min, Integer max);

    long countByStockQuantity(Integer quantity);
}