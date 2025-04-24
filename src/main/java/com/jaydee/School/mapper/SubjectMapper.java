package com.jaydee.School.mapper;

import com.jaydee.School.DTO.SubjectDTO;
import com.jaydee.School.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    SubjectDTO toDTO(Subject subject);
    Subject toEntity(SubjectDTO subjectDTO);
    List<SubjectDTO> toDTOList(List<Subject> subjects);
} 