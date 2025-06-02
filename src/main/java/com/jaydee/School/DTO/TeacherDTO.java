package com.jaydee.School.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class TeacherDTO {
	private Long id;
	
	private String name;
	
	private String subject;
	
	private String phoneNumber;
		
	private LocalDate dob;
	
	private LocalDate joinDate;
	private Long userId;
	
	private List<Long> classIds;
}
