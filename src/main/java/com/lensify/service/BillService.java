package com.lensify.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lensify.dto.bill.BillRequestDto;
import com.lensify.dto.bill.BillResponseDto;
import com.lensify.response.ApiResponse;

public interface BillService {

    ApiResponse<BillResponseDto> createBill(BillRequestDto request);

    ApiResponse<BillResponseDto> generateBillFromOrder(Long orderId);

    ApiResponse<List<BillResponseDto>> getAllBills(Pageable pageable, String search);

    ApiResponse<BillResponseDto> getBillById(Long id);

    ApiResponse<BillResponseDto> updateBill(Long id, BillRequestDto request);

    ApiResponse<String> deleteBill(Long id);

}
