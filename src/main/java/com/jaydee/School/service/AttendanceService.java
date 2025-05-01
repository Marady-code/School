package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.entity.Attendance;

public interface AttendanceService {
	
	AttendanceDTO markAttendance(AttendanceDTO attendanceDTO);
	
	List<AttendanceDTO> getAllAttendances();
	
	List<AttendanceDTO> getAttendanceByStudentId(Long studentId);
	
	Attendance updateAttendance(Long studentId,Attendance attendanceUpdate);
	
	void deleteAttendanceByStudentId(Long studentId);
//	
//	Attendance updateAttendance(Long id, Attendance AttendanceUpdate);
}
