package com.lensify.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.dto.order.OrderRequestDto;
import com.lensify.dto.order.OrderResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderResponseDto> createOrder(@Validated @RequestBody OrderRequestDto request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderId,desc") String sort,
            @RequestParam(required = false) String search
    ) {

        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.DESC;
        String sortField = "orderId";

        if (sortParts.length > 0 && !sortParts[0].isBlank()) {
            sortField = sortParts[0];
        }
        if (sortParts.length > 1) {
            direction = "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(orderService.getAllOrders(pageable, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(@PathVariable Long id,
                                                                      @RequestBody OrderRequestDto request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

}
