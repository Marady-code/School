package com.jaydee.School.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "students")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Student_id")
	private long id;

	@NotBlank(message = "First name is required")
	@Column(name = "First_name", nullable = false)
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Column(name = "Last_name", nullable = false)
	private String lastName;

	@NotBlank(message = "Gender is required")
	@Column(name = "Gender", nullable = false)
	private String gender;

	@Column(name = "Date of Birth")
	private LocalDate dob;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	@NotNull
	@Column(name = "Student_email", nullable = false, unique = true)
	private String email;

	@NotBlank(message = "Password is required")
	@NotNull
	@Column(name = "Student_password", nullable = false, unique = true)
	private String password;
}
