package com.jaydee.School.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class StudentDTO {
	private Long id;

	private String firstName;

	private String lastName;
	
	private String gender;
	
	private LocalDate dob;
	
	private String classId;
    
}
