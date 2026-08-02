package com.lensify.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LensResponseDto {

    private Long lensId;

    private String brand;

    private String lensType;

    private String lensMaterial;

    private Double power;

    private BigDecimal price;

    private Integer stock;

    private Boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getLensId() {
        return lensId;
    }

    public void setLensId(Long lensId) {
        this.lensId = lensId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLensType() {
        return lensType;
    }

    public void setLensType(String lensType) {
        this.lensType = lensType;
    }

    public String getLensMaterial() {
        return lensMaterial;
    }

    public void setLensMaterial(String lensMaterial) {
        this.lensMaterial = lensMaterial;
    }

    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
