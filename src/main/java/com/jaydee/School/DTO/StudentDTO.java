package com.jaydee.School.DTO;

import java.time.LocalDate;

import com.jaydee.School.entity.Student.Gender;

import lombok.Data;

@Data
public class StudentDTO {

	private Long id;

	private String firstName;

	private String lastName;

	private Gender gender;

	private LocalDate dob;

	private String address;
//	
//	private String email;
//	
	private String phoneNumber;

	private String emergencyContact;

	private Long classId;

}
