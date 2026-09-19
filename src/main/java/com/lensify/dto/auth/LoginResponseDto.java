package com.lensify.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private Long userId;

    private String fullName;

    private String username;
    private String email;
    private String phoneNumber;

    private String role;
    private String token;

   

}