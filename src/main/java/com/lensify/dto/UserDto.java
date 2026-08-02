package com.lensify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UserDto {

	private Long userId;

	@NotBlank(message = "Full name is required")
	private String fullName;

	@NotBlank(message = "Username is required")
	private String username;

	@Email(message = "Invalid email")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
	private String phoneNumber;

	private Boolean status;

	private String roleName;

	private String password;

	public UserDto() {
	}

	public UserDto(Long userId,
				   String fullName,
				   String username,
				   String email,
				   String phoneNumber,
				   Boolean status,
				   String roleName) {

		this.userId = userId;
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.roleName = roleName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// Generate Getters and Setters
    
    
}