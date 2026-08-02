package com.lensify.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.dto.bill.BillRequestDto;
import com.lensify.dto.bill.BillResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.BillService;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ApiResponse<BillResponseDto> createBill(@Validated @RequestBody BillRequestDto request) {
        return billService.createBill(request);
    }

    @PostMapping("/generate-from-order/{orderId}")
    public ApiResponse<BillResponseDto> generateBillFromOrder(@PathVariable Long orderId) {
        return billService.generateBillFromOrder(orderId);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BillResponseDto>>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "billId,desc") String sort,
            @RequestParam(required = false) String search
    ) {

        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.DESC;
        String sortField = "billId";

        if (sortParts.length > 0 && !sortParts[0].isBlank()) {
            sortField = sortParts[0];
        }
        if (sortParts.length > 1) {
            direction = "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(billService.getAllBills(pageable, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponseDto>> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BillResponseDto>> updateBill(@PathVariable Long id,
                                                                   @RequestBody BillRequestDto request) {
        return ResponseEntity.ok(billService.updateBill(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBill(@PathVariable Long id) {
        return ResponseEntity.ok(billService.deleteBill(id));
    }

}
