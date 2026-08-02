package com.lensify.dto.prescription;

import jakarta.validation.constraints.NotNull;

public class PrescriptionRequestDto {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    @NotNull(message = "Left eye power is required")
    private Double leftEyePower;

    @NotNull(message = "Right eye power is required")
    private Double rightEyePower;

    private String cylinder;

    private Integer axis;

    private Double pd;

    private String doctorName;

    private String remarks;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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
