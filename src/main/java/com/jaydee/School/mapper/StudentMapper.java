package com.jaydee.School.mapper;

import org.mapstruct.Mapper;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
	
//	StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
	
	Student toEntity(StudentDTO dto);
	
	StudentDTO toDTO(Student entity);
}
