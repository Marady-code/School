package com.jaydee.School.DTO;

import java.time.LocalDateTime;

import com.jaydee.School.entity.AttendanceStatus;

import lombok.Data;

@Data
public class AttendanceDTO {
	private long id;
	
	private LocalDateTime date;
	
	private AttendanceStatus status;
	
	private Long studentId;
	
	private Long teacherId;
	

	
}
