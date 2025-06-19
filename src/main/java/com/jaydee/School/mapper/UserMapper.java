package com.jaydee.School.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.entity.User;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "isFirstLogin", source = "isFirstLogin")
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "plainPassword", ignore = true)
    UserResponse mapToUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isFirstLogin", source = "isFirstLogin")
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    void updateEntityFromDTO(UserResponse dto, @MappingTarget User entity);

    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "plainPassword", ignore = true)
    UserResponse toDTO(User user);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "isFirstLogin", source = "isFirstLogin")
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserResponse userResponse);
}
