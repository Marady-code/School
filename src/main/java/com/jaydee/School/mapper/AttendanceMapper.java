package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.entity.Attendance;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
	
	AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);
	
	@Mapping(source = "student.id", target = "studentId")
	@Mapping(source = "teacher.id", target = "teacherId")
	AttendanceDTO toDTO(Attendance attendance);
	
	@Mapping(source = "studentId", target = "student.id")
	@Mapping(source = "teacherId", target = "teacher.id")
	Attendance toEntity(AttendanceDTO attendanceDTO);
	
	List<AttendanceDTO> toDTOList(List<Attendance> attendances);
	
	
}
