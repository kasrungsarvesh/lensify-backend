package com.lensify.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lensify.dto.bill.BillRequestDto;
import com.lensify.dto.bill.BillResponseDto;
import com.lensify.entity.Bill;
import com.lensify.entity.Customer;
import com.lensify.entity.Order;
import com.lensify.repository.BillRepository;
import com.lensify.repository.CustomerRepository;
import com.lensify.repository.OrderRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.BillService;
import com.lensify.repository.PaymentRepository;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    
    
    public BillServiceImpl(
            BillRepository billRepository,
            CustomerRepository customerRepository,
            OrderRepository orderRepository,
            PaymentRepository paymentRepository) {

        this.billRepository = billRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ApiResponse<BillResponseDto> createBill(BillRequestDto request) {
        log.info("Creating bill for customerId={}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Bill bill = new Bill();
        bill.setCustomer(customer);

        if (request.getOrderId() != null) {
            Order order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            bill.setOrder(order);
        }

        bill.setBillDate(LocalDateTime.now());
        bill.setSubtotal(request.getSubtotal());
        bill.setDiscount(request.getDiscount() == null ? BigDecimal.ZERO : request.getDiscount());
        bill.setGst(request.getGst() == null ? BigDecimal.ZERO : request.getGst());
        bill.setStatus("PENDING");

        // Calculate total: subtotal - discount + gst
        BigDecimal total = bill.getSubtotal()
                .subtract(bill.getDiscount())
                .add(bill.getGst());

        bill.setTotal(total);

        bill = billRepository.save(bill);

        return new ApiResponse<>(true, "Bill created successfully.", toDto(bill));
    }

    @Override
    public ApiResponse<BillResponseDto> generateBillFromOrder(Long orderId) {
        log.info("Generating bill from orderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Customer customer = order.getCustomer();

        Bill bill = new Bill();
        bill.setCustomer(customer);
        bill.setOrder(order);
        bill.setBillDate(LocalDateTime.now());
        bill.setSubtotal(order.getTotalAmount());
        bill.setDiscount(BigDecimal.ZERO);
        bill.setGst(BigDecimal.ZERO);
        bill.setTotal(order.getTotalAmount());
        bill.setStatus("PENDING");

        bill = billRepository.save(bill);

        return new ApiResponse<>(true, "Bill generated from order successfully.", toDto(bill));
    }

    @Override
    public ApiResponse<List<BillResponseDto>> getAllBills(Pageable pageable, String search) {
        log.info("Fetching bills. page: {}, size: {}, search: {}", pageable.getPageNumber(), pageable.getPageSize(), search);

        Page<Bill> page;

        if (search != null && !search.isBlank()) {
            page = billRepository.findByCustomerCustomerNameContainingIgnoreCase(search, pageable);
        } else {
            page = billRepository.findAll(pageable);
        }

        List<BillResponseDto> list = page.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Bills fetched successfully.", list);
    }

    @Override
    public ApiResponse<BillResponseDto> getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        return new ApiResponse<>(true, "Bill fetched successfully.", toDto(bill));
    }

    @Override
    public ApiResponse<BillResponseDto> updateBill(Long id, BillRequestDto request) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (request.getCustomerId() != null && !request.getCustomerId().equals(bill.getCustomer().getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            bill.setCustomer(customer);
        }

        if (request.getSubtotal() != null) {
            bill.setSubtotal(request.getSubtotal());
        }

        if (request.getDiscount() != null) {
            bill.setDiscount(request.getDiscount());
        }

        if (request.getGst() != null) {
            bill.setGst(request.getGst());
        }

        // Recalculate total
        BigDecimal total = bill.getSubtotal()
                .subtract(bill.getDiscount() == null ? BigDecimal.ZERO : bill.getDiscount())
                .add(bill.getGst() == null ? BigDecimal.ZERO : bill.getGst());

        bill.setTotal(total);

        bill = billRepository.save(bill);

        return new ApiResponse<>(true, "Bill updated successfully.", toDto(bill));
    }

    @Override
    public ApiResponse<String> deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        billRepository.delete(bill);

        return new ApiResponse<>(true, "Bill deleted successfully.", null);
    }

    private BillResponseDto toDto(Bill bill) {

        BillResponseDto dto = new BillResponseDto();

        dto.setBillId(bill.getBillId());

        dto.setCustomerId(
                bill.getCustomer() == null
                        ? null
                        : bill.getCustomer().getCustomerId()
        );

        dto.setCustomerName(
                bill.getCustomer() == null
                        ? null
                        : bill.getCustomer().getCustomerName()
        );

        dto.setOrderId(
                bill.getOrder() == null
                        ? null
                        : bill.getOrder().getOrderId()
        );

        dto.setBillDate(bill.getBillDate());

        dto.setSubtotal(bill.getSubtotal());
        dto.setDiscount(bill.getDiscount());
        dto.setGst(bill.getGst());
        dto.setTotal(bill.getTotal());

        BigDecimal paidAmount = paymentRepository
                .findByBillBillId(bill.getBillId())
                .stream()
                .map(payment -> payment.getAmount() == null
                        ? BigDecimal.ZERO
                        : payment.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = bill.getTotal() == null
                ? BigDecimal.ZERO
                : bill.getTotal();

        BigDecimal dueAmount = total.subtract(paidAmount);

        if (dueAmount.compareTo(BigDecimal.ZERO) < 0) {
            dueAmount = BigDecimal.ZERO;
        }

        dto.setPaidAmount(paidAmount);
        dto.setDueAmount(dueAmount);

        dto.setStatus(bill.getStatus());

        return dto;
    }

}
