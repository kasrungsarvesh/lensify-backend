package com.lensify.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lensify.dto.customer.CustomerRequestDto;
import com.lensify.dto.customer.CustomerResponseDto;
import com.lensify.entity.Customer;
import com.lensify.repository.CustomerRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.CustomerService;




import com.lensify.entity.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public ApiResponse<CustomerResponseDto> addCustomer(CustomerRequestDto request) {

        Customer customer = new Customer();

        customer.setCustomerName(request.getCustomerName());
        customer.setMobileNumber(request.getMobileNumber());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());

        customer = customerRepository.save(customer);

        CustomerResponseDto response = new CustomerResponseDto();

        response.setCustomerId(customer.getCustomerId());
        response.setCustomerName(customer.getCustomerName());
        response.setMobileNumber(customer.getMobileNumber());
        response.setEmail(customer.getEmail());
        response.setAddress(customer.getAddress());

        return new ApiResponse<>(
                true,
                "Customer added successfully.",
                response
        );
    }

    @Override
	public ApiResponse<List<CustomerResponseDto>> getAllCustomers() {

	    List<Customer> customers = customerRepository.findAll();

	    List<CustomerResponseDto> response = customers.stream()
	            .map(customer -> {
	                CustomerResponseDto dto = new CustomerResponseDto();

	                dto.setCustomerId(customer.getCustomerId());
	                dto.setCustomerName(customer.getCustomerName());
	                dto.setMobileNumber(customer.getMobileNumber());
	                dto.setEmail(customer.getEmail());
	                dto.setAddress(customer.getAddress());

	                return dto;
	            })
	            .toList();

	    return new ApiResponse<>(
	            true,
	            "Customers fetched successfully.",
	            response
	    );
	}

	@Override
	public ApiResponse<CustomerResponseDto> getCustomerById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<CustomerResponseDto> updateCustomer(Long id, CustomerRequestDto request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponse<String> deleteCustomer(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}