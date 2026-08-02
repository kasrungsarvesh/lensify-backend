package com.lensify.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lensify.dto.LensRequestDto;
import com.lensify.dto.LensResponseDto;
import com.lensify.entity.Lens;
import com.lensify.repository.LensRepository;
import com.lensify.response.ApiResponse;
import com.lensify.service.LensService;

@Service
public class LensServiceImpl implements LensService {

    private static final Logger log = LoggerFactory.getLogger(LensServiceImpl.class);

    private final LensRepository lensRepository;

    public LensServiceImpl(LensRepository lensRepository) {
        this.lensRepository = lensRepository;
    }

    @Override
    public ApiResponse<LensResponseDto> createLens(LensRequestDto request) {
        log.info("Creating lens: {}", request.getBrand());

        Lens lens = new Lens();

        lens.setBrand(request.getBrand());
        lens.setLensType(request.getLensType());
        lens.setLensMaterial(request.getLensMaterial());
        lens.setPower(request.getPower());
        lens.setPrice(request.getPrice());
        lens.setStock(request.getStock());
        lens.setStatus(request.getStatus() == null ? true : request.getStatus());

        lens = lensRepository.save(lens);

        LensResponseDto response = toDto(lens);

        return new ApiResponse<>(true, "Lens created successfully.", response);
    }

    @Override
    public ApiResponse<List<LensResponseDto>> getAllLenses(Pageable pageable, String search) {
        log.info("Fetching lenses. page: {}, size: {}, search: {}", pageable.getPageNumber(), pageable.getPageSize(), search);

        Page<Lens> page;

        if (search != null && !search.isBlank()) {
            page = lensRepository.findByBrandContainingIgnoreCase(search, pageable);
        } else {
            page = lensRepository.findAll(pageable);
        }

        List<LensResponseDto> list = page.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new ApiResponse<>(true, "Lenses fetched successfully.", list);
    }

    @Override
    public ApiResponse<LensResponseDto> getLensById(Long lensId) {
        Lens lens = lensRepository.findById(lensId)
                .orElseThrow(() -> new RuntimeException("Lens not found"));

        return new ApiResponse<>(true, "Lens fetched successfully.", toDto(lens));
    }

    @Override
    public ApiResponse<LensResponseDto> updateLens(Long lensId, LensRequestDto request) {
        Lens lens = lensRepository.findById(lensId)
                .orElseThrow(() -> new RuntimeException("Lens not found"));

        if (request.getBrand() != null) lens.setBrand(request.getBrand());
        if (request.getLensType() != null) lens.setLensType(request.getLensType());
        if (request.getLensMaterial() != null) lens.setLensMaterial(request.getLensMaterial());
        if (request.getPower() != null) lens.setPower(request.getPower());
        if (request.getPrice() != null) lens.setPrice(request.getPrice());
        if (request.getStock() != null) lens.setStock(request.getStock());
        if (request.getStatus() != null) lens.setStatus(request.getStatus());

        lens = lensRepository.save(lens);

        return new ApiResponse<>(true, "Lens updated successfully.", toDto(lens));
    }

    @Override
    public ApiResponse<String> deleteLens(Long lensId) {
        Lens lens = lensRepository.findById(lensId)
                .orElseThrow(() -> new RuntimeException("Lens not found"));

        lensRepository.delete(lens);

        return new ApiResponse<>(true, "Lens deleted successfully.", null);
    }

    private LensResponseDto toDto(Lens lens) {
        LensResponseDto dto = new LensResponseDto();

        dto.setLensId(lens.getLensId());
        dto.setBrand(lens.getBrand());
        dto.setLensType(lens.getLensType());
        dto.setLensMaterial(lens.getLensMaterial());
        dto.setPower(lens.getPower());
        dto.setPrice(lens.getPrice());
        dto.setStock(lens.getStock());
        dto.setStatus(lens.getStatus());
        dto.setCreatedAt(lens.getCreatedAt());
        dto.setUpdatedAt(lens.getUpdatedAt());

        return dto;
    }

}
