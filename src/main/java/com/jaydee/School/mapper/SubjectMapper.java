package com.jaydee.School.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.jaydee.School.DTO.SubjectDTO;
import com.jaydee.School.entity.Subject;
import com.jaydee.School.entity.Teacher;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    
    @Mapping(source = "teacher.id", target = "teacherId")
    SubjectDTO toDTO(Subject subject);

    @Mapping(source = "teacherId", target = "teacher", qualifiedByName = "idToTeacher")
    Subject toEntity(SubjectDTO dto);
    
    List<SubjectDTO> toDTOList(List<Subject> subjects);

    @Named("idToTeacher")
    default Teacher idToTeacher(Long id) {
        if (id == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setId(id);
        return teacher;
    }
}