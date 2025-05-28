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
		@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "subject", source = "specialization")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "classIds", ignore = true)
	TeacherDTO toTeacherDTO(Teacher entity);
	
	@Mapping(target = "firstName", ignore = true)
	@Mapping(target = "lastName", ignore = true)
	@Mapping(target = "gender", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "qualification", ignore = true)
	@Mapping(target = "specialization", source = "subject")
	@Mapping(target = "experienceYears", ignore = true)
	@Mapping(target = "isActive", ignore = true)	@Mapping(target = "subjects", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "classes", ignore = true)
	Teacher toEntity(TeacherDTO teacherDTO);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "firstName", ignore = true)
	@Mapping(target = "lastName", ignore = true)
	@Mapping(target = "gender", ignore = true)
	@Mapping(target = "email", ignore = true)
	@Mapping(target = "address", ignore = true)
	@Mapping(target = "qualification", ignore = true)
	@Mapping(target = "specialization", source = "subject")
	@Mapping(target = "experienceYears", ignore = true)
	@Mapping(target = "isActive", ignore = true)	@Mapping(target = "subjects", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "classes", ignore = true)
	void updateEntityFromDTO(TeacherDTO teacherDTO, @MappingTarget Teacher teacher);
	
	List<TeacherDTO> toDTOList(List<Teacher> teachers);
}
