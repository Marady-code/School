package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.entity.Attendance;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {	
	AttendanceDTO toDTO(Attendance attendance);
	
	Attendance toEntity(AttendanceDTO attendanceDTO);
	
	List<AttendanceDTO> toDTOList(List<Attendance> attendances);
		default Student studentIdToStudent(Long studentId) {
		if (studentId == null) {
			return null;
		}
		Student student = new Student();
		student.setId(studentId);
		return student;
	}
	
	default Teacher teacherIdToTeacher(Long teacherId) {
		if (teacherId == null) {
			return null;
		}
		Teacher teacher = new Teacher();
		teacher.setId(teacherId);
		return teacher;
	}
}
