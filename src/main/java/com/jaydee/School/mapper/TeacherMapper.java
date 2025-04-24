package com.jaydee.School.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

	TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);
	
	TeacherDTO toTeacherDTO(Teacher enitity);
	
	Teacher toEntity(TeacherDTO teacherDTO);
	
	List<TeacherDTO> toDTOList(List<Teacher> teachers);
}
