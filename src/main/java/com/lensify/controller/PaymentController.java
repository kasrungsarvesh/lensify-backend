package com.lensify.controller;

import java.util.List;

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

    // =====================================================
    // CREATE PAYMENT
    // =====================================================

    @PostMapping
    public ApiResponse<PaymentResponseDto> createPayment(
            @Validated @RequestBody PaymentRequestDto request) {

        return paymentService.createPayment(request);
    }

    // =====================================================
    // GET ALL PAYMENTS
    // =====================================================

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>>
    getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    // =====================================================
    // GET PAYMENT BY ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>>
    getPaymentById(@PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    // =====================================================
    // GET PAYMENTS BY BILL
    // =====================================================

    @GetMapping("/bill/{billId}")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>>
    getPaymentsByBill(@PathVariable Long billId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByBill(billId)
        );
    }

    // =====================================================
    // UPDATE PAYMENT
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>>
    updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentRequestDto request) {

        return ResponseEntity.ok(
                paymentService.updatePayment(id, request)
        );
    }

    // =====================================================
    // DELETE PAYMENT
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>>
    deletePayment(@PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.deletePayment(id)
        );
    }
}