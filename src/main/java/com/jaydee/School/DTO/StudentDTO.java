package com.jaydee.School.DTO;

import java.time.LocalDate;

import com.jaydee.School.entity.Student.Gender;

import lombok.Data;

@Data
public class StudentDTO {

	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private String phoneNumber;

	private Gender gender;

	private LocalDate dob;

	private String address;

	private String emergencyContact;

	private String emergencyPhone;

	private Boolean isActive;

	private Long classId;

}
