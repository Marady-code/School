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

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "teacherId", source = "teacher.id")
	@Mapping(target = "classId", source = "classEntity.id")
	StudentDTO toStudentDTO(Student entity);

	@Mapping(target = "user", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	@Mapping(target = "classEntity", ignore = true)
	@Mapping(target = "attendanceRecords", ignore = true)
	Student toEntity(StudentDTO studentDTO);

	void updateEntityFromDTO(StudentDTO studentDTO, @MappingTarget Student student);
	
	List<StudentDTO> toDTOList(List<Student> students);
}
