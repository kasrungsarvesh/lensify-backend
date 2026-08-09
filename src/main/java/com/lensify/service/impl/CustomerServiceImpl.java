package com.lensify.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.lensify.dto.customer.CustomerRequestDto;
import com.lensify.dto.customer.CustomerResponseDto;
import com.lensify.entity.Customer;
import com.lensify.repository.CustomerRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.CustomerService;
import com.lensify.exception.DuplicateResourceException;
import com.lensify.exception.ResourceNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

   @Override
public ApiResponse<CustomerResponseDto> addCustomer(CustomerRequestDto request) {

    Customer customer = new Customer();
 // Check Mobile Number
    if (customerRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
    	throw new DuplicateResourceException("Mobile number already exists.");
    }

    // Check Email
    if (request.getEmail() != null &&
        !request.getEmail().isBlank() &&
        customerRepository.findByEmail(request.getEmail()).isPresent()) {

    	throw new DuplicateResourceException("Email already exists.");
    }

    // Check Alternate Mobile
    if (request.getAlternatePhone() != null &&
        !request.getAlternatePhone().isBlank() &&
        customerRepository.findByAlternatePhone(request.getAlternatePhone()).isPresent()) {

    	throw new DuplicateResourceException("Alternate mobile number already exists.");
    }
    // Generate Customer Code
    Customer lastCustomer = customerRepository.findTopByOrderByCustomerIdDesc();
    if (lastCustomer == null || lastCustomer.getCustomerCode() == null) {
        customer.setCustomerCode("CUST000001");
    } else {
        String lastCode = lastCustomer.getCustomerCode(); // Example: CUST000010
        int number = Integer.parseInt(lastCode.replace("CUST", ""));
        customer.setCustomerCode(
            String.format("CUST%06d", number + 1)
        );
    }

    customer.setCustomerName(request.getCustomerName());
    customer.setGender(request.getGender());
    customer.setDateOfBirth(request.getDateOfBirth());
    customer.setAge(request.getAge());
    customer.setMobileNumber(request.getMobileNumber());
    customer.setAlternatePhone(request.getAlternatePhone());
    customer.setEmail(request.getEmail());
    customer.setAddress(request.getAddress());
    customer.setCity(request.getCity());
    customer.setReferenceBy(request.getReferenceBy());
    customer.setStatus(request.getStatus());

    customer = customerRepository.save(customer);

    CustomerResponseDto response = mapToDto(customer);

    return new ApiResponse<>(
            true,
            "Customer added successfully.",
            response
    );
}

    @Override
    public ApiResponse<List<CustomerResponseDto>> getAllCustomers() {

        List<CustomerResponseDto> response = customerRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(
                true,
                "Customers fetched successfully.",
                response);
    }

    @Override
    public ApiResponse<CustomerResponseDto> getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer does not exist."
                        )
                );

        return new ApiResponse<>(
                true,
                "Customer fetched successfully.",
                mapToDto(customer)
        );
    }

    @Override
    public ApiResponse<CustomerResponseDto> updateCustomer(Long id, CustomerRequestDto request) {

    	if (customerRepository.existsByMobileNumberAndCustomerIdNot(
    	        request.getMobileNumber(), id)) {

    	    throw new DuplicateResourceException("Mobile number already exists.");
    	}

    	if (request.getEmail() != null &&
    	    !request.getEmail().isBlank() &&
    	    customerRepository.existsByEmailAndCustomerIdNot(
    	            request.getEmail(), id)) {

    	    throw new DuplicateResourceException("Email already exists.");
    	}

    	if (request.getAlternatePhone() != null &&
    	    !request.getAlternatePhone().isBlank() &&
    	    customerRepository.existsByAlternatePhoneAndCustomerIdNot(
    	            request.getAlternatePhone(), id)) {

    	    throw new DuplicateResourceException("Alternate mobile number already exists.");
    	}
    	Customer customer = customerRepository.findById(id)
    	        .orElseThrow(() ->
    	            new ResourceNotFoundException("Customer does not exist."));

//        customer.setCustomerCode(request.getCustomerCode());
        customer.setCustomerName(request.getCustomerName());
        customer.setGender(request.getGender());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setAge(request.getAge());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setAlternatePhone(request.getAlternatePhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setReferenceBy(request.getReferenceBy());
        customer.setStatus(request.getStatus());

        try {

            customer = customerRepository.save(customer);

        } catch (DataIntegrityViolationException ex) {

            throw new DuplicateResourceException(
                    "Customer details already exist. Please use different mobile, email or alternate mobile."
            );

        }

        return new ApiResponse<>(
                true,
                "Customer updated successfully.",
                mapToDto(customer));
    }

    @Override
    public ApiResponse<String> deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customerRepository.delete(customer);

        return new ApiResponse<>(
                true,
                "Customer deleted successfully.",
                null);
    }

    /**
     * Convert Customer Entity to CustomerResponseDto
     */
    private CustomerResponseDto mapToDto(Customer customer) {

        CustomerResponseDto dto = new CustomerResponseDto();

        dto.setCustomerId(customer.getCustomerId());
        dto.setCustomerCode(customer.getCustomerCode());
        dto.setCustomerName(customer.getCustomerName());
        dto.setGender(customer.getGender());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setAge(customer.getAge());
        dto.setMobileNumber(customer.getMobileNumber());
        dto.setAlternatePhone(customer.getAlternatePhone());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setReferenceBy(customer.getReferenceBy());
        dto.setStatus(customer.getStatus());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        return dto;
    }
    
    @Override
    public ApiResponse<List<CustomerResponseDto>> searchCustomers(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return new ApiResponse<>(
                    true,
                    "Please enter a search keyword.",
                    List.of()
            );
        }

        List<Customer> customers =
                customerRepository.searchCustomers(keyword.trim());

        List<CustomerResponseDto> result = customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(
                true,
                "Customers fetched successfully.",
                result
        );
    }
}