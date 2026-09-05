package com.lensify.dto.bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lensify.dto.payment.PaymentRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDto {

    private Long billId;

    private Long customerId;

    private String customerName;

    private Long orderId;

    private LocalDateTime billDate;

    private BigDecimal subtotal;

    private BigDecimal discount;

    private BigDecimal gst;

    private BigDecimal total;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;

    private String status;
   

 

}
