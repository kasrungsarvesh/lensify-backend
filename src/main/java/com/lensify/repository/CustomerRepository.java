package com.lensify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.lensify.entity.Customer;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findTopByOrderByCustomerIdDesc();
	
	 Optional<Customer> findByMobileNumber(String mobileNumber);

	    Optional<Customer> findByEmail(String email);

	    Optional<Customer> findByAlternatePhone(String alternatePhone);
	    
	    boolean existsByMobileNumberAndCustomerIdNot(String mobileNumber, Long customerId);

	    boolean existsByEmailAndCustomerIdNot(String email, Long customerId);

	    boolean existsByAlternatePhoneAndCustomerIdNot(String alternatePhone, Long customerId);
	    
	    @Query("""
	    	    SELECT c FROM Customer c
	    	    WHERE LOWER(c.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))
	    	       OR LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
	    	       OR c.mobileNumber LIKE CONCAT('%', :keyword, '%')
	    	    ORDER BY c.customerName ASC
	    	""")
	    	List<Customer> searchCustomers(@Param("keyword") String keyword);
	

}