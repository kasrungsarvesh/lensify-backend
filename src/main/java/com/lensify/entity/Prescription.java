package com.lensify.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "prescriptions")
public class Prescription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "left_eye_power")
    private Double leftEyePower;

    @Column(name = "right_eye_power")
    private Double rightEyePower;

    @Column(length = 50)
    private String cylinder;

    @Column
    private Integer axis;

    @Column
    private Double pd;

    @Column(name = "doctor_name", length = 100)
    private String doctorName;

    @Column(length = 500)
    private String remarks;

    public Prescription() {
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Double getLeftEyePower() {
        return leftEyePower;
    }

    public void setLeftEyePower(Double leftEyePower) {
        this.leftEyePower = leftEyePower;
    }

    public Double getRightEyePower() {
        return rightEyePower;
    }

    public void setRightEyePower(Double rightEyePower) {
        this.rightEyePower = rightEyePower;
    }

    public String getCylinder() {
        return cylinder;
    }

    public void setCylinder(String cylinder) {
        this.cylinder = cylinder;
    }

    public Integer getAxis() {
        return axis;
    }

    public void setAxis(Integer axis) {
        this.axis = axis;
    }

    public Double getPd() {
        return pd;
    }

    public void setPd(Double pd) {
        this.pd = pd;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
