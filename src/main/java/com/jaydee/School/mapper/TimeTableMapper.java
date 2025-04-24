package com.jaydee.School.mapper;

import com.jaydee.School.DTO.TimeTableDTO;
import com.jaydee.School.entity.TimeTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeTableMapper {
    TimeTableMapper INSTANCE = Mappers.getMapper(TimeTableMapper.class);

    @Mapping(source = "classEntity.id", target = "classId")
    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "teacher.id", target = "teacherId")
    TimeTableDTO toDTO(TimeTable timeTable);

    @Mapping(source = "classId", target = "classEntity.id")
    @Mapping(source = "subjectId", target = "subject.id")
    @Mapping(source = "teacherId", target = "teacher.id")
    TimeTable toEntity(TimeTableDTO timeTableDTO);

    List<TimeTableDTO> toDTOList(List<TimeTable> timeTables);
} 