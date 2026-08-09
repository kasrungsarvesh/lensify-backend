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

    private static final Logger log =
            LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    private final PrescriptionRepository prescriptionRepository;
    private final CustomerRepository customerRepository;

    public PrescriptionServiceImpl(
            PrescriptionRepository prescriptionRepository,
            CustomerRepository customerRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.customerRepository = customerRepository;
    }

    // =====================================================
    // CREATE PRESCRIPTION
    // =====================================================

    @Override
    public ApiResponse<PrescriptionResponseDto> createPrescription(
            PrescriptionRequestDto request) {

        log.info(
                "Creating prescription for customerId={}",
                request.getCustomerId()
        );

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found.")
                );

        Prescription prescription = new Prescription();

        prescription.setCustomer(customer);

        // Prescription Information
        prescription.setPrescriptionDate(
                request.getPrescriptionDate()
        );

        prescription.setDoctorName(
                request.getDoctorName()
        );

        // Right Eye
        prescription.setRightEyeSph(
                request.getRightEyeSph()
        );

        prescription.setRightEyeCyl(
                request.getRightEyeCyl()
        );

        prescription.setRightEyeAxis(
                request.getRightEyeAxis()
        );

        prescription.setRightEyeVa(
                request.getRightEyeVa()
        );

        // Left Eye
        prescription.setLeftEyeSph(
                request.getLeftEyeSph()
        );

        prescription.setLeftEyeCyl(
                request.getLeftEyeCyl()
        );

        prescription.setLeftEyeAxis(
                request.getLeftEyeAxis()
        );

        prescription.setLeftEyeVa(
                request.getLeftEyeVa()
        );

        // PD
        prescription.setPdDistance(
                request.getPdDistance()
        );

        prescription.setPdNear(
                request.getPdNear()
        );

        // Lens
        prescription.setLensType(
                request.getLensType()
        );

        prescription.setLensIndex(
                request.getLensIndex()
        );

        prescription.setCoating(
                request.getCoating()
        );

        // Remarks
        prescription.setRemarks(
                request.getRemarks()
        );

        prescription = prescriptionRepository.save(prescription);

        log.info(
                "Prescription created successfully. prescriptionId={}",
                prescription.getPrescriptionId()
        );

        return new ApiResponse<>(
                true,
                "Prescription created successfully.",
                toDto(prescription)
        );
    }

    // =====================================================
    // GET ALL PRESCRIPTIONS
    // =====================================================

    @Override
    public ApiResponse<List<PrescriptionResponseDto>> getAllPrescriptions(
            Pageable pageable,
            String search) {

        log.info(
                "Fetching prescriptions. page={}, size={}, search={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                search
        );

        Page<Prescription> page;

        if (search != null && !search.isBlank()) {

            page = prescriptionRepository
                    .findByCustomerCustomerNameContainingIgnoreCase(
                            search,
                            pageable
                    );

        } else {

            page = prescriptionRepository.findAll(pageable);
        }

        List<PrescriptionResponseDto> list =
                page.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Prescriptions fetched successfully.",
                list
        );
    }

    // =====================================================
    // GET PRESCRIPTION BY ID
    // =====================================================

    @Override
    public ApiResponse<PrescriptionResponseDto> getPrescriptionById(
            Long id) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found."
                                )
                        );

        return new ApiResponse<>(
                true,
                "Prescription fetched successfully.",
                toDto(prescription)
        );
    }

    // =====================================================
    // UPDATE PRESCRIPTION
    // =====================================================

    @Override
    public ApiResponse<PrescriptionResponseDto> updatePrescription(
            Long id,
            PrescriptionRequestDto request) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found."
                                )
                        );

        // Customer
        if (request.getCustomerId() != null &&
                !request.getCustomerId().equals(
                        prescription.getCustomer().getCustomerId()
                )) {

            Customer customer =
                    customerRepository.findById(
                            request.getCustomerId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Customer not found."
                            )
                    );

            prescription.setCustomer(customer);
        }

        // Prescription Information
        if (request.getPrescriptionDate() != null) {
            prescription.setPrescriptionDate(
                    request.getPrescriptionDate()
            );
        }

        if (request.getDoctorName() != null) {
            prescription.setDoctorName(
                    request.getDoctorName()
            );
        }

        // Right Eye
        if (request.getRightEyeSph() != null) {
            prescription.setRightEyeSph(
                    request.getRightEyeSph()
            );
        }

        if (request.getRightEyeCyl() != null) {
            prescription.setRightEyeCyl(
                    request.getRightEyeCyl()
            );
        }

        if (request.getRightEyeAxis() != null) {
            prescription.setRightEyeAxis(
                    request.getRightEyeAxis()
            );
        }

        if (request.getRightEyeVa() != null) {
            prescription.setRightEyeVa(
                    request.getRightEyeVa()
            );
        }

        // Left Eye
        if (request.getLeftEyeSph() != null) {
            prescription.setLeftEyeSph(
                    request.getLeftEyeSph()
            );
        }

        if (request.getLeftEyeCyl() != null) {
            prescription.setLeftEyeCyl(
                    request.getLeftEyeCyl()
            );
        }

        if (request.getLeftEyeAxis() != null) {
            prescription.setLeftEyeAxis(
                    request.getLeftEyeAxis()
            );
        }

        if (request.getLeftEyeVa() != null) {
            prescription.setLeftEyeVa(
                    request.getLeftEyeVa()
            );
        }

        // PD
        if (request.getPdDistance() != null) {
            prescription.setPdDistance(
                    request.getPdDistance()
            );
        }

        if (request.getPdNear() != null) {
            prescription.setPdNear(
                    request.getPdNear()
            );
        }

        // Lens
        if (request.getLensType() != null) {
            prescription.setLensType(
                    request.getLensType()
            );
        }

        if (request.getLensIndex() != null) {
            prescription.setLensIndex(
                    request.getLensIndex()
            );
        }

        if (request.getCoating() != null) {
            prescription.setCoating(
                    request.getCoating()
            );
        }

        // Remarks
        if (request.getRemarks() != null) {
            prescription.setRemarks(
                    request.getRemarks()
            );
        }

        prescription =
                prescriptionRepository.save(prescription);

        log.info(
                "Prescription updated successfully. prescriptionId={}",
                prescription.getPrescriptionId()
        );

        return new ApiResponse<>(
                true,
                "Prescription updated successfully.",
                toDto(prescription)
        );
    }

    // =====================================================
    // DELETE PRESCRIPTION
    // =====================================================

    @Override
    public ApiResponse<String> deletePrescription(Long id) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found."
                                )
                        );

        prescriptionRepository.delete(prescription);

        log.info(
                "Prescription deleted successfully. prescriptionId={}",
                id
        );

        return new ApiResponse<>(
                true,
                "Prescription deleted successfully.",
                null
        );
    }

    // =====================================================
    // ENTITY → DTO
    // =====================================================

    private PrescriptionResponseDto toDto(
            Prescription prescription) {

        PrescriptionResponseDto dto =
                new PrescriptionResponseDto();

        dto.setPrescriptionId(
                prescription.getPrescriptionId()
        );

        if (prescription.getCustomer() != null) {

            dto.setCustomerId(
                    prescription.getCustomer().getCustomerId()
            );
            dto.setCustomerCode(
            		prescription.getCustomer() == null ? null : prescription.getCustomer().getCustomerCode()
            	);


            dto.setCustomerName(
                    prescription.getCustomer().getCustomerName()
            );
        }

        // Prescription Information
        dto.setPrescriptionDate(
                prescription.getPrescriptionDate()
        );

        dto.setDoctorName(
                prescription.getDoctorName()
        );

        // Right Eye
        dto.setRightEyeSph(
                prescription.getRightEyeSph()
        );

        dto.setRightEyeCyl(
                prescription.getRightEyeCyl()
        );

        dto.setRightEyeAxis(
                prescription.getRightEyeAxis()
        );

        dto.setRightEyeVa(
                prescription.getRightEyeVa()
        );

        // Left Eye
        dto.setLeftEyeSph(
                prescription.getLeftEyeSph()
        );

        dto.setLeftEyeCyl(
                prescription.getLeftEyeCyl()
        );

        dto.setLeftEyeAxis(
                prescription.getLeftEyeAxis()
        );

        dto.setLeftEyeVa(
                prescription.getLeftEyeVa()
        );

        // PD
        dto.setPdDistance(
                prescription.getPdDistance()
        );

        dto.setPdNear(
                prescription.getPdNear()
        );

        // Lens
        dto.setLensType(
                prescription.getLensType()
        );

        dto.setLensIndex(
                prescription.getLensIndex()
        );

        dto.setCoating(
                prescription.getCoating()
        );

        // Remarks
        dto.setRemarks(
                prescription.getRemarks()
        );

        // Audit
        dto.setCreatedAt(
                prescription.getCreatedAt()
        );

        return dto;
    }
    @Override
    public ApiResponse<Long> getPrescriptionCountByCustomer(Long customerId) {

        long count = prescriptionRepository
                .countByCustomerCustomerId(customerId);

        return new ApiResponse<>(
                true,
                "Prescription count fetched successfully.",
                count
        );
    }
    
    @Override
    public ApiResponse<List<PrescriptionResponseDto>> getPrescriptionsByCustomer(
            Long customerId) {

        List<Prescription> prescriptions =
                prescriptionRepository
                        .findByCustomerCustomerIdOrderByPrescriptionDateDesc(
                                customerId
                        );

        List<PrescriptionResponseDto> result =
                prescriptions.stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Customer prescriptions fetched successfully.",
                result
        );
    }
}