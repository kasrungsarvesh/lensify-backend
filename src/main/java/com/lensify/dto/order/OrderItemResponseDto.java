package com.lensify.dto.order;

import java.math.BigDecimal;

public class OrderItemResponseDto {

    private Long orderItemId;

    private Long lensId;

    private String lensBrand;

    private Integer quantity;

    private BigDecimal price;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getLensId() {
        return lensId;
    }

    public void setLensId(Long lensId) {
        this.lensId = lensId;
    }

    public String getLensBrand() {
        return lensBrand;
    }

    public void setLensBrand(String lensBrand) {
        this.lensBrand = lensBrand;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
