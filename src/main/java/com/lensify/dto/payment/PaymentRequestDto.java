package com.lensify.dto.payment;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    @NotNull(message = "Bill id is required")
    private Long billId;

    private String paymentType;

    @NotNull(message = "Payment amount is required")
    @Min(value = 0, message = "Payment amount must be non-negative")
    private BigDecimal amount;

    private String status;
}