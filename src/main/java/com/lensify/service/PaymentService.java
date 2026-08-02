package com.lensify.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lensify.dto.payment.PaymentRequestDto;
import com.lensify.dto.payment.PaymentResponseDto;
import com.lensify.response.ApiResponse;

public interface PaymentService {

    ApiResponse<PaymentResponseDto> createPayment(PaymentRequestDto request);

    ApiResponse<List<PaymentResponseDto>> getAllPayments(Pageable pageable);

    ApiResponse<List<PaymentResponseDto>> getPaymentsByBillId(Long billId, Pageable pageable);

    ApiResponse<PaymentResponseDto> getPaymentById(Long id);

    ApiResponse<PaymentResponseDto> updatePayment(Long id, PaymentRequestDto request);

    ApiResponse<String> deletePayment(Long id);

}
