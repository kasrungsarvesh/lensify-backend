package com.lensify.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.lensify.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Customer findTopByOrderByCustomerIdDesc();
	
	 Optional<Customer> findByMobileNumber(String mobileNumber);

	    Optional<Customer> findByEmail(String email);

	    Optional<Customer> findByAlternatePhone(String alternatePhone);
	    
	    boolean existsByMobileNumberAndCustomerIdNot(String mobileNumber, Long customerId);

	    boolean existsByEmailAndCustomerIdNot(String email, Long customerId);

	    boolean existsByAlternatePhoneAndCustomerIdNot(String alternatePhone, Long customerId);
	

}