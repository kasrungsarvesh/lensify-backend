package com.lensify.service;

import java.util.List;

import com.lensify.dto.UserDto;
import com.lensify.response.ApiResponse;

public interface UserService {

    ApiResponse<UserDto> createUser(UserDto userDto);

    ApiResponse<List<UserDto>> getAllUsers();

    ApiResponse<UserDto> getUserById(Long userId);

    ApiResponse<UserDto> updateUser(Long userId, UserDto userDto);

    ApiResponse<String> deleteUser(Long userId);

}