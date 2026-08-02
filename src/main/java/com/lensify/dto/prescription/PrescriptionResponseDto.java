package com.lensify.dto.prescription;

import java.time.LocalDateTime;

public class PrescriptionResponseDto {

    private Long prescriptionId;

    private Long customerId;

    private String customerName;

    private Double leftEyePower;

    private Double rightEyePower;

    private String cylinder;

    private Integer axis;

    private Double pd;

    private String doctorName;

    private String remarks;

    private LocalDateTime createdAt;

    // Getters and Setters

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
