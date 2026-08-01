package com.lensify.service;

import java.util.List;

import com.lensify.dto.customer.CustomerRequestDto;
import com.lensify.dto.customer.CustomerResponseDto;
import com.lensify.response.ApiResponse;

public interface CustomerService {

    ApiResponse<CustomerResponseDto> addCustomer(CustomerRequestDto request);

    ApiResponse<List<CustomerResponseDto>> getAllCustomers();

    ApiResponse<CustomerResponseDto> getCustomerById(Long id);

    ApiResponse<CustomerResponseDto> updateCustomer(Long id, CustomerRequestDto request);

    ApiResponse<String> deleteCustomer(Long id);
    
}