package com.lensify.dto.order;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class OrderRequestDto {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    private String status;

    @NotNull(message = "Order items are required")
    private List<OrderItemRequestDto> items;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDto> items) {
        this.items = items;
    }

}
