package com.lensify.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.dto.customer.CustomerRequestDto;
import com.lensify.dto.customer.CustomerResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ApiResponse<CustomerResponseDto> addCustomer(
            @Validated @RequestBody CustomerRequestDto request) {

        return customerService.addCustomer(request);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDto>>> getAllCustomers() {

        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    
}