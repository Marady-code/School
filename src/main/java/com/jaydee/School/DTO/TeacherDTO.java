package com.jaydee.School.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.jaydee.School.entity.Teacher.Gender;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private Gender gender;
	private LocalDate dob;
	private String address;
	private String qualification;
	private String specialization;
	private Integer experienceYears;
	private LocalDate joinDate;
	private Boolean isActive;
	private List<Long> classIds;
	private Long userId;
	private String role;
	private Boolean passwordChangeRequired;
	private LocalDate lastPasswordChangeDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
