package com.jaydee.School.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateRequest {

	@NotBlank(message = "UserName is required")
	@Size(min = 4, message = "UserName must be at least 4 characters")
	private String userName;

	@Email(message = "Invalid email format")
	private String email;

	private String phoneNumber;

	private String newPassword;
}