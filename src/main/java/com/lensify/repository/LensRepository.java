package com.lensify.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lensify.entity.Lens;

@Repository
public interface LensRepository extends JpaRepository<Lens, Long> {

    Page<Lens> findByBrandContainingIgnoreCase(String brand, Pageable pageable);

    long countByStockLessThan(int stock);

}
