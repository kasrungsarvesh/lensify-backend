package com.lensify.dto.customer;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDto {

    // =========================
    // CUSTOMER INFORMATION
    // =========================

    @NotBlank(message = "Customer name is required.")
    private String customerName;

    private String gender;

    private LocalDate dateOfBirth;

    private Integer age;


    // =========================
    // MOBILE INFORMATION
    // =========================

    @NotBlank(message = "Mobile number is required.")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Mobile must be 10 digits."
    )
    private String mobileNumber;

    @Pattern(
        regexp = "^$|^[0-9]{10}$",
        message = "Alternate mobile must be 10 digits."
    )
    private String alternatePhone;


    // =========================
    // CONTACT INFORMATION
    // =========================

    @Email(message = "Invalid email.")
    private String email;

    private String address;

    private String city;

    private String referenceBy;


    // =========================
    // STATUS
    // =========================

    @NotNull(message = "Status is required.")
    private Boolean status;
}