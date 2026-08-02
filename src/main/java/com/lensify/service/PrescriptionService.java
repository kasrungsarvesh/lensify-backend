package com.lensify.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lensify.dto.prescription.PrescriptionRequestDto;
import com.lensify.dto.prescription.PrescriptionResponseDto;
import com.lensify.response.ApiResponse;

public interface PrescriptionService {

    ApiResponse<PrescriptionResponseDto> createPrescription(PrescriptionRequestDto request);

    ApiResponse<List<PrescriptionResponseDto>> getAllPrescriptions(Pageable pageable, String search);

    ApiResponse<PrescriptionResponseDto> getPrescriptionById(Long id);

    ApiResponse<PrescriptionResponseDto> updatePrescription(Long id, PrescriptionRequestDto request);

    ApiResponse<String> deletePrescription(Long id);

}
