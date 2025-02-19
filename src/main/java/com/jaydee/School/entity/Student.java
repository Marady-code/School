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
import lombok.Data;

@Data
@Entity
@Table(name = "students")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Student_id")
	private long id;

	@NotBlank(message = "First name is required")
	@Column(name = "First_name")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Column(name = "Last_name")
	private String lastName;

	@NotBlank(message = "Gender is required")
	@NotBlank(message = "Gender should be either male or female")
	@Column(name = "Gender")
	private String gender;

	@Column(name = "Date of Birth")
	private LocalDate dob;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	@Column(name = "student_email")
	private String email;

	@NotBlank(message = "Password is required")
	@Column(name = "student_password")
	private String password;
}
