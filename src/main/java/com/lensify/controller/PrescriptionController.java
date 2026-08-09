package com.lensify.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.dto.prescription.PrescriptionRequestDto;
import com.lensify.dto.prescription.PrescriptionResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.PrescriptionService;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public ApiResponse<PrescriptionResponseDto> createPrescription(@Validated @RequestBody PrescriptionRequestDto request) {
        return prescriptionService.createPrescription(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>> getAllPrescriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prescriptionId,desc") String sort,
            @RequestParam(required = false) String search
    ) {

        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.DESC;
        String sortField = "prescriptionId";

        if (sortParts.length > 0 && !sortParts[0].isBlank()) {
            sortField = sortParts[0];
        }
        if (sortParts.length > 1) {
            direction = "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(pageable, search));
    }
    @GetMapping("/customer/{customerId}/count")
    public ResponseEntity<ApiResponse<Long>> getPrescriptionCountByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                prescriptionService.getPrescriptionCountByCustomer(customerId)
        );
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<PrescriptionResponseDto>>>
    getPrescriptionsByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                prescriptionService.getPrescriptionsByCustomer(customerId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponseDto>> updatePrescription(@PathVariable Long id,
                                                                                   @RequestBody PrescriptionRequestDto request) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.deletePrescription(id));
    }
    

}
