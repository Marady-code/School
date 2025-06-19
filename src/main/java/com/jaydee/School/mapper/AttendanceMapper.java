package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.entity.Attendance;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "date", source = "date")
	@Mapping(target = "status", source = "status")
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "teacherId", source = "teacher.id")
	AttendanceDTO toDTO(Attendance attendance);

	@Mapping(target = "id", source = "id")
	@Mapping(target = "date", source = "date")
	@Mapping(target = "status", source = "status")
	@Mapping(target = "student", source = "studentId", qualifiedByName = "studentIdToStudent")
	@Mapping(target = "teacher", source = "teacherId", qualifiedByName = "teacherIdToTeacher")
	Attendance toEntity(AttendanceDTO attendanceDTO);

	List<AttendanceDTO> toDTOList(List<Attendance> attendances);

	@org.mapstruct.Named("studentIdToStudent")
	default Student studentIdToStudent(Long studentId) {
		if (studentId == null) {
			return null;
		}
		Student student = new Student();
		student.setId(studentId);
		return student;
	}

	@org.mapstruct.Named("teacherIdToTeacher")
	default Teacher teacherIdToTeacher(Long teacherId) {
		if (teacherId == null) {
			return null;
		}
		Teacher teacher = new Teacher();
		teacher.setId(teacherId);
		return teacher;
	}
}
