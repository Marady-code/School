package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "firstName", source = "user.firstName")
	@Mapping(target = "lastName", source = "user.lastName")
	@Mapping(target = "email", source = "user.email")
	@Mapping(target = "phoneNumber", source = "user.phoneNumber")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "emergencyContact", source = "emergencyContact")
	@Mapping(target = "emergencyPhone", source = "emergencyPhone")
	@Mapping(target = "isActive", source = "isActive")
	@Mapping(target = "classId", source = "classEntity.id")
	StudentDTO toStudentDTO(Student student);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "emergencyContact", source = "emergencyContact")
	@Mapping(target = "emergencyPhone", source = "emergencyPhone")
	@Mapping(target = "isActive", source = "isActive")
	@Mapping(target = "classEntity", ignore = true)
	@Mapping(target = "attendanceRecords", ignore = true)
	@Mapping(target = "examResults", ignore = true)
	@Mapping(target = "performanceReports", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Student toEntity(StudentDTO studentDTO);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user.firstName", source = "firstName")
	@Mapping(target = "user.lastName", source = "lastName")
	@Mapping(target = "user.phoneNumber", source = "phoneNumber")
	@Mapping(target = "user.email", source = "email")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "emergencyContact", source = "emergencyContact")
	@Mapping(target = "emergencyPhone", source = "emergencyPhone")
	@Mapping(target = "isActive", source = "isActive")
	@Mapping(target = "classEntity", ignore = true)
	@Mapping(target = "attendanceRecords", ignore = true)
	@Mapping(target = "examResults", ignore = true)
	@Mapping(target = "performanceReports", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void updateEntityFromDTO(StudentDTO studentDTO, @MappingTarget Student student);

	List<StudentDTO> toDTOList(List<Student> students);
}
