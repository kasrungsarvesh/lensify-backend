package com.lensify.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lensify.dto.order.OrderItemRequestDto;
import com.lensify.dto.order.OrderItemResponseDto;
import com.lensify.dto.order.OrderRequestDto;
import com.lensify.dto.order.OrderResponseDto;
import com.lensify.entity.Customer;
import com.lensify.entity.Lens;
import com.lensify.entity.Order;
import com.lensify.entity.OrderItem;
import com.lensify.repository.CustomerRepository;
import com.lensify.repository.LensRepository;
import com.lensify.repository.OrderRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final LensRepository lensRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            LensRepository lensRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.lensRepository = lensRepository;
    }

    @Override
    public ApiResponse<OrderResponseDto> createOrder(OrderRequestDto request) {
        log.info("Creating order for customerId={}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(request.getStatus() == null ? "PENDING" : request.getStatus());
        order.setItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (OrderItemRequestDto itemReq : request.getItems()) {
                Lens lens = lensRepository.findById(itemReq.getLensId())
                        .orElseThrow(() -> new RuntimeException("Lens not found"));

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setLens(lens);
                item.setQuantity(itemReq.getQuantity());
                item.setPrice(itemReq.getPrice());

                order.getItems().add(item);

                BigDecimal itemTotal = itemReq.getPrice().multiply(new BigDecimal(itemReq.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        return new ApiResponse<>(true, "Order created successfully.", toDto(order));
    }

    @Override
    public ApiResponse<List<OrderResponseDto>> getAllOrders(Pageable pageable, String search) {
        log.info("Fetching orders. page: {}, size: {}, search: {}", pageable.getPageNumber(), pageable.getPageSize(), search);

        Page<Order> page;

        if (search != null && !search.isBlank()) {
            page = orderRepository.findByCustomerCustomerNameContainingIgnoreCase(search, pageable);
        } else {
            page = orderRepository.findAll(pageable);
        }

        List<OrderResponseDto> list = page.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Orders fetched successfully.", list);
    }

    @Override
    public ApiResponse<OrderResponseDto> getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return new ApiResponse<>(true, "Order fetched successfully.", toDto(order));
    }

    @Override
    public ApiResponse<OrderResponseDto> updateOrder(Long id, OrderRequestDto request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (request.getCustomerId() != null && !request.getCustomerId().equals(order.getCustomer().getCustomerId())) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            order.setCustomer(customer);
        }

        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            order.getItems().clear();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (OrderItemRequestDto itemReq : request.getItems()) {
                Lens lens = lensRepository.findById(itemReq.getLensId())
                        .orElseThrow(() -> new RuntimeException("Lens not found"));

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setLens(lens);
                item.setQuantity(itemReq.getQuantity());
                item.setPrice(itemReq.getPrice());

                order.getItems().add(item);

                BigDecimal itemTotal = itemReq.getPrice().multiply(new BigDecimal(itemReq.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }

            order.setTotalAmount(totalAmount);
        }

        order = orderRepository.save(order);

        return new ApiResponse<>(true, "Order updated successfully.", toDto(order));
    }

    @Override
    public ApiResponse<String> deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepository.delete(order);

        return new ApiResponse<>(true, "Order deleted successfully.", null);
    }

    private OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();

        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer() == null ? null : order.getCustomer().getCustomerId());
        dto.setCustomerName(order.getCustomer() == null ? null : order.getCustomer().getCustomerName());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponseDto> items = order.getItems().stream()
                .map(item -> {
                    OrderItemResponseDto itemDto = new OrderItemResponseDto();
                    itemDto.setOrderItemId(item.getOrderItemId());
                    itemDto.setLensId(item.getLens().getLensId());
                    itemDto.setLensBrand(item.getLens().getBrand());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                })
                .collect(Collectors.toList());

        dto.setItems(items);

        return dto;
    }

}
