package com.jaydee.School.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.UserDTO;
import com.jaydee.School.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDTO toUserDTO(User entity);

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "teacher", ignore = true)
	User toEntity(UserDTO userDTO);

	void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);
}
