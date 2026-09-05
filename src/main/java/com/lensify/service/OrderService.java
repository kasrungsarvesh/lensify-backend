package com.lensify.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lensify.dto.OrderUpdateRequestDto;
import com.lensify.dto.order.OrderRequestDto;
import com.lensify.dto.order.OrderResponseDto;
import com.lensify.response.ApiResponse;

public interface OrderService {

    ApiResponse<OrderResponseDto> createOrder(OrderRequestDto request);

    ApiResponse<List<OrderResponseDto>> getAllOrders(Pageable pageable, String search);

    ApiResponse<OrderResponseDto> getOrderById(Long id);

    ApiResponse<OrderResponseDto> updateOrder(Long id, OrderRequestDto request);

    ApiResponse<String> deleteOrder(Long id);
    
//    ApiResponse<OrderResponseDto> updateOrder(
//            Long orderId,
//            OrderUpdateRequestDto request
//    );
}
