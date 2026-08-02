package com.lensify.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.dto.payment.PaymentRequestDto;
import com.lensify.dto.payment.PaymentResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ApiResponse<PaymentResponseDto> createPayment(@Validated @RequestBody PaymentRequestDto request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentId,desc") String sort
    ) {

        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.DESC;
        String sortField = "paymentId";

        if (sortParts.length > 0 && !sortParts[0].isBlank()) {
            sortField = sortParts[0];
        }
        if (sortParts.length > 1) {
            direction = "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(paymentService.getAllPayments(pageable));
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentsByBillId(
            @PathVariable Long billId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "paymentId"));

        return ResponseEntity.ok(paymentService.getPaymentsByBillId(billId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> updatePayment(@PathVariable Long id,
                                                                         @RequestBody PaymentRequestDto request) {
        return ResponseEntity.ok(paymentService.updatePayment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.deletePayment(id));
    }

}
