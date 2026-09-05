package com.lensify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(nullable = false, length = 100)
    private String customerName;

    @Column(nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(unique = true)
    private String email;

    @Column(length = 300)
    private String address;
    @Column(name = "customer_code")
    private String customerCode;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private Integer age;

    @Column(name = "alternate_phone")
    private String alternatePhone;

    private String city;

    @Column(name = "reference_by")
    private String referenceBy;

    private Boolean status;

}