package com.lensify.dto.bill;

import java.math.BigDecimal;

import com.lensify.dto.payment.PaymentRequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
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

    
}
