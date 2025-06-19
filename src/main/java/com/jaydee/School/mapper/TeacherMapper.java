package com.jaydee.School.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

	@Mapping(target = "id", source = "id")
	@Mapping(target = "firstName", source = "user.firstName")
	@Mapping(target = "lastName", source = "user.lastName")
	@Mapping(target = "email", source = "user.email")
	@Mapping(target = "phoneNumber", source = "user.phoneNumber")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "qualification", source = "qualification")
	@Mapping(target = "specialization", source = "specialization")
	@Mapping(target = "experienceYears", source = "experienceYears")
	@Mapping(target = "joinDate", source = "joinDate")
	@Mapping(target = "isActive", source = "user.isActive")
	@Mapping(target = "classIds", expression = "java(entity.getClasses() != null ? entity.getClasses().stream().map(c -> c.getId()).collect(java.util.stream.Collectors.toList()) : null)")
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "role", source = "user.role")
	@Mapping(target = "passwordChangeRequired", source = "user.passwordChangeRequired")
	@Mapping(target = "lastPasswordChangeDate", source = "user.lastPasswordChangeDate")
	@Mapping(target = "createdAt", source = "createdAt")
	@Mapping(target = "updatedAt", source = "updatedAt")
	TeacherDTO toTeacherDTO(Teacher entity);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user.firstName", source = "firstName")
	@Mapping(target = "user.lastName", source = "lastName")
	@Mapping(target = "user.phoneNumber", source = "phoneNumber")
	@Mapping(target = "user.email", source = "email")
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "qualification", source = "qualification")
	@Mapping(target = "specialization", source = "specialization")
	@Mapping(target = "experienceYears", source = "experienceYears")
	@Mapping(target = "joinDate", source = "joinDate")
	@Mapping(target = "classes", ignore = true)
	@Mapping(target = "subjects", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void updateEntityFromDTO(TeacherDTO dto, @MappingTarget Teacher teacher);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "gender", source = "gender")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "qualification", source = "qualification")
	@Mapping(target = "specialization", source = "specialization")
	@Mapping(target = "experienceYears", source = "experienceYears")
	@Mapping(target = "joinDate", source = "joinDate")
	@Mapping(target = "classes", ignore = true)
	@Mapping(target = "subjects", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "firstName", source = "firstName")
	@Mapping(target = "lastName", source = "lastName")
	@Mapping(target = "user.firstName", source = "firstName")
	@Mapping(target = "user.lastName", source = "lastName")
	Teacher toEntity(TeacherDTO teacherDTO);

	default List<TeacherDTO> toDTOList(List<Teacher> teachers) {
		if (teachers == null) {
			return null;
		}
		return teachers.stream()
				.map(this::toTeacherDTO)
				.collect(Collectors.toList());
	}
}
