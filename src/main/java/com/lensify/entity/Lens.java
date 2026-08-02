package com.lensify.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lenses")
public class Lens extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lens_id")
    private Long lensId;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(name = "lens_type", length = 100)
    private String lensType;

    @Column(name = "lens_material", length = 100)
    private String lensMaterial;

    @Column
    private Double power;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private Integer stock;

    @Column
    private Boolean status = true;

    public Lens() {
    }

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

}
