package com.lensify.dto.prescription;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequestDto {

    // =========================
    // CUSTOMER
    // =========================

    @NotNull(message = "Customer is required.")
    private Long customerId;

    // =========================
    // PRESCRIPTION INFORMATION
    // =========================

    @NotNull(message = "Prescription date is required.")
    private LocalDate prescriptionDate;

    @Size(max = 100, message = "Doctor name cannot exceed 100 characters.")
    private String doctorName;

    // =========================
    // RIGHT EYE
    // =========================

    @NotNull(message = "Right eye SPH is required.")
    private Double rightEyeSph;

    private Double rightEyeCyl;

    @Min(value = 0, message = "Right eye AXIS cannot be negative.")
    @Max(value = 180, message = "Right eye AXIS cannot exceed 180.")
    private Integer rightEyeAxis;

    @Size(max = 20, message = "Right eye VA cannot exceed 20 characters.")
    private String rightEyeVa;

    // =========================
    // LEFT EYE
    // =========================

    @NotNull(message = "Left eye SPH is required.")
    private Double leftEyeSph;

    private Double leftEyeCyl;

    @Min(value = 0, message = "Left eye AXIS cannot be negative.")
    @Max(value = 180, message = "Left eye AXIS cannot exceed 180.")
    private Integer leftEyeAxis;

    @Size(max = 20, message = "Left eye VA cannot exceed 20 characters.")
    private String leftEyeVa;

    // =========================
    // PD
    // =========================

    private Double pdDistance;

    private Double pdNear;

    // =========================
    // LENS INFORMATION
    // =========================

    @Size(max = 50, message = "Lens type cannot exceed 50 characters.")
    private String lensType;

    @Size(max = 10, message = "Lens index cannot exceed 10 characters.")
    private String lensIndex;

    @Size(max = 100, message = "Coating cannot exceed 100 characters.")
    private String coating;

    // =========================
    // REMARKS
    // =========================

    @Size(max = 500, message = "Remarks cannot exceed 500 characters.")
    private String remarks;
}