package com.jaydee.School.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

//	@Mappings({
//			@Mapping(source = "firstName", target = "first_name"),
//			@Mapping(source = "lastName", target = "last_name"),
//	})
	StudentDTO toStudentDTO(Student entity);

//	@Mappings({
//			@Mapping(source = "first_name", target = "firstName"),
//			@Mapping(source = "last_name", target = "lastName"),
//			@Mapping(source = "dob", target = "dob"),
//			@Mapping(source = "email", target = "email"),
//			@Mapping(source = "password", target = "password")
//	})
	Student toEntity(StudentDTO studentDTO);
}
