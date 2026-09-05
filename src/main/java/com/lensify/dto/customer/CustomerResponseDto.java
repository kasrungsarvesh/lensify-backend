package com.lensify.dto.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private Long customerId;

    private String customerCode;

    private String customerName;

    private String gender;

    private LocalDate dateOfBirth;

    private Integer age;

    private String mobileNumber;

    private String alternatePhone;

    private String email;

    private String address;

    private String city;

    private String referenceBy;

    private Boolean status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    
}