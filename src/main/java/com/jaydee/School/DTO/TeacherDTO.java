package com.jaydee.School.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class TeacherDTO {

	private long id;
	
	private String name;
	
	private String subject;
	
	private String phoneNumber;
		
	private LocalDate dob;
	
	private LocalDate joinDate;
	
	private long userId;
	
	private List<Long> studentIds;
	
	private List<Long> classIds;
}
