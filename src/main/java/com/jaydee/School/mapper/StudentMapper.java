package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

	@Mapping(target = "id", source = "id")
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "lastName", source = "lastName")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "classId", source = "classEntity.id")
	StudentDTO toStudentDTO(Student entity);
	@Mapping(target = "id", source = "id")
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "lastName", source = "lastName") 
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "classEntity", ignore = true)
	@Mapping(target = "attendanceRecords", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "phoneNumber", ignore = true)
	@Mapping(target = "emergencyContact", ignore = true)
	@Mapping(target = "emergencyPhone", ignore = true)
	@Mapping(target = "isActive", ignore = true)
	@Mapping(target = "examResults", ignore = true)
	@Mapping(target = "performanceReports", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Student toEntity(StudentDTO studentDTO);
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "lastName", source = "lastName")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "classEntity", ignore = true)
	@Mapping(target = "attendanceRecords", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "phoneNumber", ignore = true)
	@Mapping(target = "emergencyContact", ignore = true)
	@Mapping(target = "emergencyPhone", ignore = true)
	@Mapping(target = "isActive", ignore = true)
	@Mapping(target = "examResults", ignore = true)
	@Mapping(target = "performanceReports", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void updateEntityFromDTO(StudentDTO studentDTO, @MappingTarget Student student);
	
	List<StudentDTO> toDTOList(List<Student> students);
}
