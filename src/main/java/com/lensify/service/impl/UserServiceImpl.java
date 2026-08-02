package com.lensify.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lensify.dto.UserDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.UserService;
import com.lensify.entity.User;
import com.lensify.entity.Role;
import com.lensify.repository.UserRepository;
import com.lensify.repository.RoleRepository;
import com.lensify.constants.MessageConstants;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse<UserDto> createUser(UserDto userDto) {

        // Check duplicates
        if (userRepository.existsByUsername(userDto.getUsername())
                || userRepository.existsByEmail(userDto.getEmail())
                || userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {

            return new ApiResponse<>(
                    false,
                    MessageConstants.USER_ALREADY_EXISTS,
                    null
            );
        }

        Role role = roleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();

        user.setFullName(userDto.getFullName());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(userDto.getStatus() == null ? true : userDto.getStatus());
        user.setRole(role);

        user = userRepository.save(user);

        UserDto response = toDto(user);

        return new ApiResponse<>(true, MessageConstants.USER_CREATED, response);
    }

    @Override
    public ApiResponse<List<UserDto>> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserDto> response = users.stream()
                .map(this::toDto)
                .toList();

        return new ApiResponse<>(true, "Users fetched successfully.", response);
    }

    @Override
    public ApiResponse<UserDto> getUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ApiResponse<>(true, "User fetched successfully.", toDto(user));
    }

    @Override
    public ApiResponse<UserDto> updateUser(Long userId, UserDto userDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check uniqueness if username/email/phone changed
        if (userDto.getUsername() != null && !userDto.getUsername().equals(user.getUsername())
                && userRepository.existsByUsername(userDto.getUsername())) {
            return new ApiResponse<>(false, MessageConstants.USER_ALREADY_EXISTS, null);
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(userDto.getEmail())) {
            return new ApiResponse<>(false, MessageConstants.USER_ALREADY_EXISTS, null);
        }

        if (userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().equals(user.getPhoneNumber())
                && userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            return new ApiResponse<>(false, MessageConstants.USER_ALREADY_EXISTS, null);
        }

        user.setFullName(userDto.getFullName() == null ? user.getFullName() : userDto.getFullName());
        user.setUsername(userDto.getUsername() == null ? user.getUsername() : userDto.getUsername());
        user.setEmail(userDto.getEmail() == null ? user.getEmail() : userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber() == null ? user.getPhoneNumber() : userDto.getPhoneNumber());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        if (userDto.getRoleName() != null) {
            Role role = roleRepository.findByRoleName(userDto.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.setRole(role);
        }
        if (userDto.getStatus() != null) {
            user.setStatus(userDto.getStatus());
        }

        user = userRepository.save(user);

        return new ApiResponse<>(true, MessageConstants.UPDATE_SUCCESS, toDto(user));
    }

    @Override
    public ApiResponse<String> deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

        return new ApiResponse<>(true, MessageConstants.DELETE_SUCCESS, null);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();

        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setStatus(user.getStatus());
        dto.setRoleName(user.getRole() == null ? null : user.getRole().getRoleName());

        return dto;
    }

}