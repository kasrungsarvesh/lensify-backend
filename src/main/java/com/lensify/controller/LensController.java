package com.lensify.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.dto.LensRequestDto;
import com.lensify.dto.LensResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.LensService;

@RestController
@RequestMapping("/api/v1/lenses")
public class LensController {

    private final LensService lensService;

    public LensController(LensService lensService) {
        this.lensService = lensService;
    }

    @PostMapping
    public ApiResponse<LensResponseDto> createLens(@Validated @RequestBody LensRequestDto request) {
        return lensService.createLens(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LensResponseDto>>> getAllLenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lensId,asc") String sort,
            @RequestParam(required = false) String search
    ) {

        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        String sortField = "lensId";

        if (sortParts.length > 0 && !sortParts[0].isBlank()) {
            sortField = sortParts[0];
        }
        if (sortParts.length > 1) {
            direction = "desc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(lensService.getAllLenses(pageable, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LensResponseDto>> getLensById(@PathVariable Long id) {
        return ResponseEntity.ok(lensService.getLensById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LensResponseDto>> updateLens(@PathVariable Long id,
                                                                    @RequestBody LensRequestDto request) {
        return ResponseEntity.ok(lensService.updateLens(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLens(@PathVariable Long id) {
        return ResponseEntity.ok(lensService.deleteLens(id));
    }

}
