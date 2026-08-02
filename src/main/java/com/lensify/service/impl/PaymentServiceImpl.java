package com.lensify.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lensify.dto.payment.PaymentRequestDto;
import com.lensify.dto.payment.PaymentResponseDto;
import com.lensify.entity.Bill;
import com.lensify.entity.Payment;
import com.lensify.repository.BillRepository;
import com.lensify.repository.PaymentRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BillRepository billRepository) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    @Override
    public ApiResponse<PaymentResponseDto> createPayment(PaymentRequestDto request) {
        log.info("Creating payment for billId={}", request.getBillId());

        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setPaymentType(request.getPaymentType());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("SUCCESS");

        payment = paymentRepository.save(payment);

        return new ApiResponse<>(true, "Payment created successfully.", toDto(payment));
    }

    @Override
    public ApiResponse<List<PaymentResponseDto>> getAllPayments(Pageable pageable) {
        log.info("Fetching all payments. page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Payment> page = paymentRepository.findAll(pageable);

        List<PaymentResponseDto> list = page.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Payments fetched successfully.", list);
    }

    @Override
    public ApiResponse<List<PaymentResponseDto>> getPaymentsByBillId(Long billId, Pageable pageable) {
        log.info("Fetching payments for billId={}", billId);

        Page<Payment> page = paymentRepository.findByBillBillId(billId, pageable);

        List<PaymentResponseDto> list = page.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Payments fetched successfully.", list);
    }

    @Override
    public ApiResponse<PaymentResponseDto> getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return new ApiResponse<>(true, "Payment fetched successfully.", toDto(payment));
    }

    @Override
    public ApiResponse<PaymentResponseDto> updatePayment(Long id, PaymentRequestDto request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (request.getBillId() != null && !request.getBillId().equals(payment.getBill().getBillId())) {
            Bill bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() -> new RuntimeException("Bill not found"));
            payment.setBill(bill);
        }

        if (request.getPaymentType() != null) {
            payment.setPaymentType(request.getPaymentType());
        }

        if (request.getAmount() != null) {
            payment.setAmount(request.getAmount());
        }

        payment = paymentRepository.save(payment);

        return new ApiResponse<>(true, "Payment updated successfully.", toDto(payment));
    }

    @Override
    public ApiResponse<String> deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentRepository.delete(payment);

        return new ApiResponse<>(true, "Payment deleted successfully.", null);
    }

    private PaymentResponseDto toDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();

        dto.setPaymentId(payment.getPaymentId());
        dto.setBillId(payment.getBill() == null ? null : payment.getBill().getBillId());
        dto.setCustomerId(payment.getBill() == null || payment.getBill().getCustomer() == null 
                ? null : payment.getBill().getCustomer().getCustomerId());
        dto.setCustomerName(payment.getBill() == null || payment.getBill().getCustomer() == null 
                ? null : payment.getBill().getCustomer().getCustomerName());
        dto.setPaymentType(payment.getPaymentType());
        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setStatus(payment.getStatus());

        return dto;
    }

}
