package com.lensify.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lensify.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Page<Payment> findByBillBillId(Long billId, Pageable pageable);

    Page<Payment> findByPaymentType(String paymentType, Pageable pageable);

}
