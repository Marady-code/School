package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

	TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);
	
	TeacherDTO toTeacherDTO(Teacher entity);
	
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "students", ignore = true)
	@Mapping(target = "classes", ignore = true)
	Teacher toEntity(TeacherDTO teacherDTO);
	
	void updateEntityFromDTO(TeacherDTO teacherDTO, @MappingTarget Teacher teacher);
	
	List<TeacherDTO> toDTOList(List<Teacher> teachers);
}
