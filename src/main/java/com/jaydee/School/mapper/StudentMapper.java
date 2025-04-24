package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

	StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

	StudentDTO toStudentDTO(Student entity);

	Student toEntity(StudentDTO studentDTO);
	
	List<StudentDTO> toDTOList(List<Student> students);
}
