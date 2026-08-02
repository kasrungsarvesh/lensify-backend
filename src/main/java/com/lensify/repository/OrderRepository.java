package com.lensify.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lensify.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerCustomerNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Order> findByStatus(String status, Pageable pageable);

    long countByStatus(String status);

}
