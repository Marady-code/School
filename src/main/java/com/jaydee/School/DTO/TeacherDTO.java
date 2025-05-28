package com.jaydee.School.DTO;

import java.time.LocalDate;
import java.util.List;

import com.jaydee.School.entity.Teacher.Gender;

import lombok.Data;

@Data
public class TeacherDTO {
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private Gender gender;
	
	private String subject;
	
	private String phoneNumber;
		
	private LocalDate dob;
	
	private LocalDate joinDate;
	private Long userId;
	
	private List<Long> classIds;
}
