package com.lensify.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lensify.constants.ApiConstants;
import com.lensify.dto.auth.LoginRequestDto;
import com.lensify.dto.auth.LoginResponseDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ApiConstants.AUTH)
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {

        return authService.login(request);

    }

}