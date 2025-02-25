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
import org.springframework.format.annotation.DateTimeFormat;

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
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotBlank(message = "Gender is required")
	@Column(name = "gender", nullable = false)
	private String gender;

	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dob;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	@Column(name = "student_email", nullable = false, unique = true)
	private String email;

	@NotBlank(message = "Password is required")
	@Column(name = "student_password", nullable = false, unique = true)
	private String password;
	
//	@Column(name = "Name")
//	private String getFullName() {
//        return firstName + " " + lastName;
//    }
}
