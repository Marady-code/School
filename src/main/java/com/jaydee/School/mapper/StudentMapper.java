package com.jaydee.School.mapper;

import org.mapstruct.Mapper;
import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

	@Mappings({
			@Mapping(source = "id", target = "id"),
			@Mapping(source = "firstName", target = "first_name"),
			@Mapping(source = "lastName", target = "last_name"),
			@Mapping(source = "gender", target = "gender")
	})
	StudentDTO toStudentDTO(Student entity);

	@Mappings({
			@Mapping(source = "id", target = "id"),
			@Mapping(source = "first_name", target = "firstName"),
			@Mapping(source = "last_name", target = "lastName"),
			@Mapping(source = "gender", target = "gender")
	})
	Student toEntity(StudentDTO studentDTO);
}
