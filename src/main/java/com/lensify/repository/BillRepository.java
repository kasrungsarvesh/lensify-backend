package com.lensify.repository;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lensify.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findByCustomerCustomerNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT COALESCE(SUM(b.total), 0) FROM Bill b")
    BigDecimal getTotalRevenue();

}
