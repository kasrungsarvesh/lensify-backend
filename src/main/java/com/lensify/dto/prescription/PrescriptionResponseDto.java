package com.lensify.dto.prescription;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDto {

    // =========================
    // PRESCRIPTION
    // =========================

    private Long prescriptionId;

    // =========================
    // CUSTOMER
    // =========================

    private Long customerId;
    private String customerCode;


    private String customerName;

    // =========================
    // PRESCRIPTION INFORMATION
    // =========================

    private LocalDate prescriptionDate;

    private String doctorName;

    // =========================
    // RIGHT EYE
    // =========================

    private Double rightEyeSph;

    private Double rightEyeCyl;

    private Integer rightEyeAxis;

    private String rightEyeVa;

    // =========================
    // LEFT EYE
    // =========================

    private Double leftEyeSph;

    private Double leftEyeCyl;

    private Integer leftEyeAxis;

    private String leftEyeVa;

    // =========================
    // PD
    // =========================

    private Double pdDistance;

    private Double pdNear;

    // =========================
    // LENS INFORMATION
    // =========================

    private String lensType;

    private String lensIndex;

    private String coating;

    // =========================
    // REMARKS
    // =========================

    private String remarks;

    // =========================
    // AUDIT
    // =========================

    private LocalDateTime createdAt;
}