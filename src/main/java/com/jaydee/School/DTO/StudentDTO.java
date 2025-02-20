package com.jaydee.School.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentDTO {
	//private Long id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String gender;
	
	private LocalDate dob;
	
	private String email;
	
	private String password;

}
