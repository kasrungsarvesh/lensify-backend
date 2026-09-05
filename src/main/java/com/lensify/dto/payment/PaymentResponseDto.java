package com.lensify.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private Long paymentId;

    private Long billId;

    private String paymentType;

    private BigDecimal amount;

    private LocalDateTime paymentDate;

    private String status;
}