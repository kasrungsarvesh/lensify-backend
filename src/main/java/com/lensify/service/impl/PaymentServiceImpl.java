package com.lensify.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            BillRepository billRepository) {

        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    @Override
    @Transactional
    public ApiResponse<PaymentResponseDto> createPayment(
            PaymentRequestDto request) {

        // Find bill
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        BigDecimal paymentAmount = request.getAmount() == null
                ? BigDecimal.ZERO
                : request.getAmount();

        BigDecimal billTotal = bill.getTotal() == null
                ? BigDecimal.ZERO
                : bill.getTotal();

        // Get all previous payments
        BigDecimal alreadyPaid = paymentRepository
                .findByBillBillId(bill.getBillId())
                .stream()
                .map(payment -> payment.getAmount() == null
                        ? BigDecimal.ZERO
                        : payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate remaining amount
        BigDecimal remainingAmount =
                billTotal.subtract(alreadyPaid);

        // Prevent overpayment
        if (paymentAmount.compareTo(remainingAmount) > 0) {
            throw new RuntimeException(
                    "Payment amount cannot exceed due amount. "
                    + "Remaining due: ₹"
                    + remainingAmount);
        }

        // Create payment
        Payment payment = new Payment();

        payment.setBill(bill);

        payment.setPaymentType(
                request.getPaymentType() == null
                        ? "CASH"
                        : request.getPaymentType()
        );

        payment.setAmount(paymentAmount);

        payment.setPaymentDate(LocalDateTime.now());

        // Calculate total paid after this payment
        BigDecimal totalPaid =
                alreadyPaid.add(paymentAmount);

        // Calculate bill status
        String billStatus;

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {

            billStatus = "PENDING";

        } else if (totalPaid.compareTo(billTotal) >= 0) {

            billStatus = "PAID";

        } else {

            billStatus = "PARTIAL";
        }

        payment.setStatus(billStatus);

        // Save payment
        payment = paymentRepository.save(payment);

        // Update bill status
        bill.setStatus(billStatus);

        billRepository.save(bill);

        return new ApiResponse<>(
                true,
                "Payment created successfully.",
                toDto(payment)
        );
    }

    @Override
    public ApiResponse<PaymentResponseDto> getPaymentById(
            Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        return new ApiResponse<>(
                true,
                "Payment fetched successfully.",
                toDto(payment)
        );
    }

    @Override
    public ApiResponse<List<PaymentResponseDto>> getPaymentsByBill(
            Long billId) {

        // Make sure bill exists
        billRepository.findById(billId)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        List<PaymentResponseDto> payments =
                paymentRepository.findByBillBillId(billId)
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Payments fetched successfully.",
                payments
        );
    }

    @Override
    public ApiResponse<List<PaymentResponseDto>> getAllPayments() {

        List<PaymentResponseDto> payments =
                paymentRepository.findAll()
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Payments fetched successfully.",
                payments
        );
    }

    @Override
    @Transactional
    public ApiResponse<PaymentResponseDto> updatePayment(
            Long paymentId,
            PaymentRequestDto request) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        Bill bill = payment.getBill();

        if (request.getBillId() != null
                && !request.getBillId()
                        .equals(bill.getBillId())) {

            bill = billRepository.findById(request.getBillId())
                    .orElseThrow(() ->
                            new RuntimeException("Bill not found"));

            payment.setBill(bill);
        }

        if (request.getPaymentType() != null) {
            payment.setPaymentType(
                    request.getPaymentType());
        }

        if (request.getAmount() != null) {
            payment.setAmount(
                    request.getAmount());
        }

        // Recalculate all payments for the bill
        List<Payment> payments =
                paymentRepository.findByBillBillId(
                        bill.getBillId());

        BigDecimal totalPaid = payments.stream()
                .map(p -> p.getAmount() == null
                        ? BigDecimal.ZERO
                        : p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate status
        BigDecimal billTotal = bill.getTotal() == null
                ? BigDecimal.ZERO
                : bill.getTotal();

        String status;

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {

            status = "PENDING";

        } else if (totalPaid.compareTo(billTotal) >= 0) {

            status = "PAID";

        } else {

            status = "PARTIAL";
        }

        payment.setStatus(status);

        payment = paymentRepository.save(payment);

        bill.setStatus(status);
        billRepository.save(bill);

        return new ApiResponse<>(
                true,
                "Payment updated successfully.",
                toDto(payment)
        );
    }

    @Override
    @Transactional
    public ApiResponse<String> deletePayment(
            Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        Bill bill = payment.getBill();

        paymentRepository.delete(payment);

        // Recalculate bill status after deleting payment
        List<Payment> remainingPayments =
                paymentRepository.findByBillBillId(
                        bill.getBillId());

        BigDecimal totalPaid = remainingPayments.stream()
                .map(p -> p.getAmount() == null
                        ? BigDecimal.ZERO
                        : p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal billTotal = bill.getTotal() == null
                ? BigDecimal.ZERO
                : bill.getTotal();

        String status;

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {

            status = "PENDING";

        } else if (totalPaid.compareTo(billTotal) >= 0) {

            status = "PAID";

        } else {

            status = "PARTIAL";
        }

        bill.setStatus(status);
        billRepository.save(bill);

        return new ApiResponse<>(
                true,
                "Payment deleted successfully.",
                null
        );
    }

    private PaymentResponseDto toDto(Payment payment) {

        PaymentResponseDto dto =
                new PaymentResponseDto();

        dto.setPaymentId(
                payment.getPaymentId());

        dto.setBillId(
                payment.getBill() == null
                        ? null
                        : payment.getBill().getBillId());

        dto.setPaymentType(
                payment.getPaymentType());

        dto.setAmount(
                payment.getAmount());

        dto.setPaymentDate(
                payment.getPaymentDate());

        dto.setStatus(
                payment.getStatus());

        return dto;
    }
}