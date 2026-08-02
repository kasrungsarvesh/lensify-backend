package com.lensify.dto.bill;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BillRequestDto {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    private Long orderId;

    @NotNull(message = "Subtotal is required")
    @Min(value = 0, message = "Subtotal must be non-negative")
    private BigDecimal subtotal;

    @Min(value = 0, message = "Discount must be non-negative")
    private BigDecimal discount;

    @Min(value = 0, message = "GST must be non-negative")
    private BigDecimal gst;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getGst() {
        return gst;
    }

    public void setGst(BigDecimal gst) {
        this.gst = gst;
    }

}
