package com.jaydee.School.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.jaydee.School.DTO.ClassDTO;
import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.entity.Student;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    ClassMapper INSTANCE = Mappers.getMapper(ClassMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "className", source = "className")
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "studentIds", source = "students", qualifiedByName = "mapStudentIds")
    @Mapping(target = "academicYear", source = "academicYear")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "capacity", source = "capacity")
    @Mapping(target = "roomNumber", source = "roomNumber")
    @Mapping(target = "description", source = "description")
    ClassDTO toDTO(ClassEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "className", source = "className")
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "academicYear", source = "academicYear")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "capacity", source = "capacity")
    @Mapping(target = "roomNumber", source = "roomNumber")
    @Mapping(target = "description", source = "description")
    ClassEntity toEntity(ClassDTO dto);

    @Named("mapStudentIds")
    default List<Long> mapStudentIds(List<Student> students) {
        if (students == null) {
            return null;
        }
        return students.stream()
                .map(Student::getId)
                .collect(Collectors.toList());
    }

    default List<ClassDTO> toDTOList(List<ClassEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
