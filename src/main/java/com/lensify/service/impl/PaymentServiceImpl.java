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
import com.lensify.entity.Order;
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

    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    @Override
    @Transactional
    public ApiResponse<PaymentResponseDto> createPayment(
            PaymentRequestDto request) {

        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BigDecimal paymentAmount =
                request.getAmount() == null
                        ? BigDecimal.ZERO
                        : request.getAmount();

        if (paymentAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Payment amount cannot be negative.");
        }

        BigDecimal billTotal =
                bill.getTotal() == null
                        ? BigDecimal.ZERO
                        : bill.getTotal();

        // ---------------------------------------------------------
        // Existing payments
        // ---------------------------------------------------------

        BigDecimal alreadyPaid =
                paymentRepository
                        .findByBillBillId(bill.getBillId())
                        .stream()
                        .map(payment ->
                                payment.getAmount() == null
                                        ? BigDecimal.ZERO
                                        : payment.getAmount())
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        BigDecimal remainingAmount =
                billTotal.subtract(alreadyPaid);

        if (paymentAmount.compareTo(remainingAmount) > 0) {

            throw new RuntimeException(
                    "Payment amount cannot exceed due amount. "
                    + "Remaining due: ₹"
                    + remainingAmount);
        }

        // ---------------------------------------------------------
        // Create payment
        // ---------------------------------------------------------

        Payment payment = new Payment();

        payment.setBill(bill);

        payment.setPaymentType(
                request.getPaymentType() == null
                        ? "CASH"
                        : request.getPaymentType());

        payment.setAmount(paymentAmount);

        payment.setPaymentDate(LocalDateTime.now());

        // ---------------------------------------------------------
        // Calculate final payment status
        // ---------------------------------------------------------

        BigDecimal totalPaid =
                alreadyPaid.add(paymentAmount);

        String status =
                calculatePaymentStatus(
                        totalPaid,
                        billTotal);

        payment.setStatus(status);

        payment = paymentRepository.save(payment);

        // ---------------------------------------------------------
        // Update BILL status
        // ---------------------------------------------------------

        bill.setStatus(status);

        billRepository.save(bill);

        // ---------------------------------------------------------
        // IMPORTANT:
        // Update ORDER status also
        // ---------------------------------------------------------

        updateOrderStatus(bill, status);

        return new ApiResponse<>(
                true,
                "Payment created successfully.",
                toDto(payment));
    }

    // =========================================================
    // GET PAYMENT BY ID
    // =========================================================

    @Override
    public ApiResponse<PaymentResponseDto> getPaymentById(
            Long paymentId) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Payment not found"));

        return new ApiResponse<>(
                true,
                "Payment fetched successfully.",
                toDto(payment));
    }

    // =========================================================
    // GET PAYMENTS BY BILL
    // =========================================================

    @Override
    public ApiResponse<List<PaymentResponseDto>> getPaymentsByBill(
            Long billId) {

        billRepository.findById(billId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Bill not found"));

        List<PaymentResponseDto> payments =
                paymentRepository
                        .findByBillBillId(billId)
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Payments fetched successfully.",
                payments);
    }

    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

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
                payments);
    }

    // =========================================================
    // UPDATE PAYMENT
    // =========================================================

    @Override
    @Transactional
    public ApiResponse<PaymentResponseDto> updatePayment(
            Long paymentId,
            PaymentRequestDto request) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Payment not found"));

        Bill oldBill = payment.getBill();

        // ---------------------------------------------------------
        // If bill is changed
        // ---------------------------------------------------------

        Bill bill = oldBill;

        if (request.getBillId() != null
                && !request.getBillId()
                        .equals(oldBill.getBillId())) {

            bill = billRepository.findById(
                    request.getBillId())
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Bill not found"));

            payment.setBill(bill);
        }

        // ---------------------------------------------------------
        // Update payment fields
        // ---------------------------------------------------------

        if (request.getPaymentType() != null) {
            payment.setPaymentType(
                    request.getPaymentType());
        }

        if (request.getAmount() != null) {

            if (request.getAmount()
                    .compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "Payment amount cannot be negative.");
            }

            payment.setAmount(
                    request.getAmount());
        }

        // ---------------------------------------------------------
        // Save first so calculation uses latest payment amount
        // ---------------------------------------------------------

        paymentRepository.save(payment);

        // ---------------------------------------------------------
        // Recalculate old bill if payment was moved
        // ---------------------------------------------------------

        if (!oldBill.getBillId()
                .equals(bill.getBillId())) {

            recalculateBillAndOrderStatus(oldBill);
        }

        // ---------------------------------------------------------
        // Recalculate new bill
        // ---------------------------------------------------------

        String status =
                recalculateBillAndOrderStatus(bill);

        payment.setStatus(status);

        paymentRepository.save(payment);

        return new ApiResponse<>(
                true,
                "Payment updated successfully.",
                toDto(payment));
    }

    // =========================================================
    // DELETE PAYMENT
    // =========================================================

    @Override
    @Transactional
    public ApiResponse<String> deletePayment(
            Long paymentId) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Payment not found"));

        Bill bill = payment.getBill();

        paymentRepository.delete(payment);

        // Recalculate bill/order after deletion
        recalculateBillAndOrderStatus(bill);

        return new ApiResponse<>(
                true,
                "Payment deleted successfully.",
                null);
    }

    // =========================================================
    // CALCULATE STATUS
    // =========================================================

    private String calculatePaymentStatus(
            BigDecimal totalPaid,
            BigDecimal billTotal) {

        if (totalPaid.compareTo(BigDecimal.ZERO) == 0) {

            return "PENDING";

        } else if (totalPaid.compareTo(billTotal) >= 0) {

            return "PAID";

        } else {

            return "PARTIAL";
        }
    }

    // =========================================================
    // UPDATE BILL + ORDER STATUS
    // =========================================================

    private void updateOrderStatus(
            Bill bill,
            String status) {

        if (bill.getOrder() != null) {

            Order order = bill.getOrder();

            order.setStatus(status);
        }
    }

    // =========================================================
    // RECALCULATE BILL + ORDER
    // =========================================================

    private String recalculateBillAndOrderStatus(
            Bill bill) {

        List<Payment> payments =
                paymentRepository
                        .findByBillBillId(
                                bill.getBillId());

        BigDecimal totalPaid =
                payments.stream()
                        .map(payment ->
                                payment.getAmount() == null
                                        ? BigDecimal.ZERO
                                        : payment.getAmount())
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        BigDecimal billTotal =
                bill.getTotal() == null
                        ? BigDecimal.ZERO
                        : bill.getTotal();

        String status =
                calculatePaymentStatus(
                        totalPaid,
                        billTotal);

        // Update bill
        bill.setStatus(status);

        billRepository.save(bill);

        // Update order
        updateOrderStatus(bill, status);

        return status;
    }

    // =========================================================
    // DTO MAPPING
    // =========================================================

    private PaymentResponseDto toDto(
            Payment payment) {

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