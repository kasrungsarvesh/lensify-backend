package com.lensify.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lensify.dto.LensRequestDto;
import com.lensify.dto.LensResponseDto;
import com.lensify.response.ApiResponse;

public interface LensService {

    ApiResponse<LensResponseDto> createLens(LensRequestDto request);

    ApiResponse<List<LensResponseDto>> getAllLenses(Pageable pageable, String search);

    ApiResponse<LensResponseDto> getLensById(Long lensId);

    ApiResponse<LensResponseDto> updateLens(Long lensId, LensRequestDto request);

    ApiResponse<String> deleteLens(Long lensId);

}
