package com.lensify.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lensify.dto.prescription.PrescriptionRequestDto;
import com.lensify.dto.prescription.PrescriptionResponseDto;
import com.lensify.entity.Customer;
import com.lensify.entity.Prescription;
import com.lensify.repository.CustomerRepository;
import com.lensify.repository.PrescriptionRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.PrescriptionService;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    private final PrescriptionRepository prescriptionRepository;
    private final CustomerRepository customerRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository,
                                   CustomerRepository customerRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ApiResponse<PrescriptionResponseDto> createPrescription(PrescriptionRequestDto request) {
        log.info("Creating prescription for customerId={}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Prescription p = new Prescription();
        p.setCustomer(customer);
        p.setLeftEyePower(request.getLeftEyePower());
        p.setRightEyePower(request.getRightEyePower());
        p.setCylinder(request.getCylinder());
        p.setAxis(request.getAxis());
        p.setPd(request.getPd());
        p.setDoctorName(request.getDoctorName());
        p.setRemarks(request.getRemarks());

        p = prescriptionRepository.save(p);

        return new ApiResponse<>(true, "Prescription created successfully.", toDto(p));
    }

    @Override
    public ApiResponse<List<PrescriptionResponseDto>> getAllPrescriptions(Pageable pageable, String search) {
        log.info("Fetching prescriptions. page: {}, size: {}, search: {}", pageable.getPageNumber(), pageable.getPageSize(), search);

        Page<Prescription> page;

        if (search != null && !search.isBlank()) {
            page = prescriptionRepository.findByCustomerCustomerNameContainingIgnoreCase(search, pageable);
        } else {
            page = prescriptionRepository.findAll(pageable);
        }

        List<PrescriptionResponseDto> list = page.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Prescriptions fetched successfully.", list);
    }

    @Override
    public ApiResponse<PrescriptionResponseDto> getPrescriptionById(Long id) {
        Prescription p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        return new ApiResponse<>(true, "Prescription fetched successfully.", toDto(p));
    }

    @Override
    public ApiResponse<PrescriptionResponseDto> updatePrescription(Long id, PrescriptionRequestDto request) {
        Prescription p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        if (request.getCustomerId() != null && !request.getCustomerId().equals(p.getCustomer().getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            p.setCustomer(customer);
        }

        if (request.getLeftEyePower() != null) p.setLeftEyePower(request.getLeftEyePower());
        if (request.getRightEyePower() != null) p.setRightEyePower(request.getRightEyePower());
        if (request.getCylinder() != null) p.setCylinder(request.getCylinder());
        if (request.getAxis() != null) p.setAxis(request.getAxis());
        if (request.getPd() != null) p.setPd(request.getPd());
        if (request.getDoctorName() != null) p.setDoctorName(request.getDoctorName());
        if (request.getRemarks() != null) p.setRemarks(request.getRemarks());

        p = prescriptionRepository.save(p);

        return new ApiResponse<>(true, "Prescription updated successfully.", toDto(p));
    }

    @Override
    public ApiResponse<String> deletePrescription(Long id) {
        Prescription p = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        prescriptionRepository.delete(p);

        return new ApiResponse<>(true, "Prescription deleted successfully.", null);
    }

    private PrescriptionResponseDto toDto(Prescription p) {
        PrescriptionResponseDto dto = new PrescriptionResponseDto();

        dto.setPrescriptionId(p.getPrescriptionId());
        dto.setCustomerId(p.getCustomer() == null ? null : p.getCustomer().getCustomerId());
        dto.setCustomerName(p.getCustomer() == null ? null : p.getCustomer().getCustomerName());
        dto.setLeftEyePower(p.getLeftEyePower());
        dto.setRightEyePower(p.getRightEyePower());
        dto.setCylinder(p.getCylinder());
        dto.setAxis(p.getAxis());
        dto.setPd(p.getPd());
        dto.setDoctorName(p.getDoctorName());
        dto.setRemarks(p.getRemarks());
        dto.setCreatedAt(p.getCreatedAt());

        return dto;
    }

}
