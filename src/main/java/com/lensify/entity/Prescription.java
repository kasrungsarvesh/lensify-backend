package com.lensify.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

  

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

  

    @Column(name = "prescription_date")
    private LocalDate prescriptionDate;

    @Column(name = "doctor_name", length = 100)
    private String doctorName;

   

    @Column(name = "right_eye_sph")
    private Double rightEyeSph;

    @Column(name = "right_eye_cyl")
    private Double rightEyeCyl;

    @Column(name = "right_eye_axis")
    private Integer rightEyeAxis;

    @Column(name = "right_eye_va", length = 20)
    private String rightEyeVa;



    @Column(name = "left_eye_sph")
    private Double leftEyeSph;

    @Column(name = "left_eye_cyl")
    private Double leftEyeCyl;

    @Column(name = "left_eye_axis")
    private Integer leftEyeAxis;

    @Column(name = "left_eye_va", length = 20)
    private String leftEyeVa;



    @Column(name = "pd_distance")
    private Double pdDistance;

    @Column(name = "pd_near")
    private Double pdNear;

 

    @Column(name = "lens_type", length = 50)
    private String lensType;

    @Column(name = "lens_index", length = 10)
    private String lensIndex;

    @Column(name = "coating", length = 100)
    private String coating;



    @Column(length = 500)
    private String remarks;
}