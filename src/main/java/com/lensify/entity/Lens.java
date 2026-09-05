package com.lensify.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
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


}
