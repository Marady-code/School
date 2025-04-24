package com.jaydee.School.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.UserDTO;
import com.jaydee.School.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	UserDTO toUserDTO(User entity);

	User toEntity(UserDTO UserDTO);
}
