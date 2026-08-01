package com.lensify.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lensify.dto.UserDto;
import com.lensify.response.ApiResponse;
import com.lensify.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public ApiResponse<UserDto> createUser(UserDto userDto) {

        return null;
    }

    @Override
    public ApiResponse<List<UserDto>> getAllUsers() {

        return null;
    }

    @Override
    public ApiResponse<UserDto> getUserById(Long userId) {

        return null;
    }

    @Override
    public ApiResponse<UserDto> updateUser(Long userId, UserDto userDto) {

        return null;
    }

    @Override
    public ApiResponse<String> deleteUser(Long userId) {

        return null;
    }

}