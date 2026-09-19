package com.lensify.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lensify.constants.MessageConstants;
import com.lensify.dto.auth.ChangePasswordRequestDto;
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

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDto request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElse(null);

        if (user == null) {

            return new ApiResponse<>(
                    false,
                    MessageConstants.INVALID_CREDENTIALS,
                    null
            );
        }

        // Verify password using BCrypt
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return new ApiResponse<>(
                    false,
                    MessageConstants.INVALID_CREDENTIALS,
                    null
            );
        }

        // Check account status
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
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole().getRoleName());

        String token = jwtUtil.generateToken(user.getUsername());

        response.setToken(token);

        return new ApiResponse<>(
                true,
                MessageConstants.LOGIN_SUCCESS,
                response
        );
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    @Override
    public ApiResponse<String> changePassword(
            String username,
            ChangePasswordRequestDto request) {

        // Find currently logged-in user
        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {

            return new ApiResponse<>(
                    false,
                    "User not found.",
                    null
            );
        }

        // Check whether current password is correct
        boolean currentPasswordMatches =
                passwordEncoder.matches(
                        request.getCurrentPassword(),
                        user.getPassword()
                );

        if (!currentPasswordMatches) {

            return new ApiResponse<>(
                    false,
                    "Current password is incorrect.",
                    null
            );
        }

        // Prevent using the same password
        boolean samePassword =
                passwordEncoder.matches(
                        request.getNewPassword(),
                        user.getPassword()
                );

        if (samePassword) {

            return new ApiResponse<>(
                    false,
                    "New password must be different from current password.",
                    null
            );
        }

        // Encode new password using BCrypt
        String encodedPassword =
                passwordEncoder.encode(
                        request.getNewPassword()
                );

        user.setPassword(encodedPassword);

        userRepository.save(user);

        return new ApiResponse<>(
                true,
                "Password changed successfully.",
                null
        );
    }
}