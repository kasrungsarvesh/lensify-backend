package com.lensify.service.impl;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lensify.constants.MessageConstants;
import com.lensify.dto.auth.LoginRequestDto;
import com.lensify.dto.auth.LoginResponseDto;
import com.lensify.entity.User;
import com.lensify.repository.UserRepository;
import com.lensify.response.ApiResponse;
import com.lensify.security.JwtUtil;
import com.lensify.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDto request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {
            return new ApiResponse<>(
                    false,
                    MessageConstants.INVALID_CREDENTIALS,
                    null
            );
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ApiResponse<>(
                    false,
                    MessageConstants.INVALID_CREDENTIALS,
                    null
            );
        }

        if (!user.getStatus()) {
            return new ApiResponse<>(
                    false,
                    "User account is inactive.",
                    null
            );
        }

        LoginResponseDto response = new LoginResponseDto();

        response.setUserId(user.getUserId());
        response.setFullName(user.getFullName());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().getRoleName());
        String token = jwtUtil.generateToken(user.getUsername());
        response.setToken(token);

        return new ApiResponse<>(
                true,
                MessageConstants.LOGIN_SUCCESS,
                response
        );
    }
}