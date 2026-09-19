package com.lensify.service;

import com.lensify.dto.auth.ChangePasswordRequestDto;
import com.lensify.dto.auth.LoginRequestDto;
import com.lensify.dto.auth.LoginResponseDto;
import com.lensify.response.ApiResponse;

public interface AuthService {

    ApiResponse<LoginResponseDto> login(LoginRequestDto request);

    ApiResponse<String> changePassword(
            String username,
            ChangePasswordRequestDto request
    );
}