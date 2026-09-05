package com.lensify.service;

import java.util.List;

import com.lensify.dto.payment.PaymentRequestDto;
import com.lensify.dto.payment.PaymentResponseDto;
import com.lensify.response.ApiResponse;

public interface PaymentService {

    ApiResponse<PaymentResponseDto> createPayment(
            PaymentRequestDto request);

    ApiResponse<PaymentResponseDto> getPaymentById(
            Long paymentId);

    ApiResponse<List<PaymentResponseDto>> getPaymentsByBill(
            Long billId);

    ApiResponse<List<PaymentResponseDto>> getAllPayments();

    ApiResponse<PaymentResponseDto> updatePayment(
            Long paymentId,
            PaymentRequestDto request);

    ApiResponse<String> deletePayment(
            Long paymentId);
}