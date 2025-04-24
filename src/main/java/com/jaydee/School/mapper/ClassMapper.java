package com.jaydee.School.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jaydee.School.DTO.ClassDTO;
import com.jaydee.School.entity.ClassEntity;

@Mapper(componentModel = "spring")
public interface ClassMapper {
//	@Mapping(source = "className", target = "className")
//    @Mapping(source = "teacher.id", target = "teacherId")
//    @Mapping(source = "students", target = "studentIds", qualifiedByName = "mapStudentList")
//    ClassDTO toDTO(ClassEntity classEntity);
//
//    @Mapping(source = "className", target = "className")
//    @Mapping(source = "teacherId", target = "teacher.id")
//    @Mapping(source = "studentIds", target = "students", qualifiedByName = "mapStudentIds")
//    ClassEntity toEntity(ClassDTO classDTO);
//
//    @Named("mapStudentList")
//    default List<Long> mapStudentsToIds(List<Student> students) {
//        if (students == null) return new ArrayList<>();
//        return students.stream().map(Student::getId).collect(Collectors.toList());
//    }
//
//    @Named("mapStudentIds")
//    default List<Student> mapIdsToStudents(List<Long> studentIds) {
//        if (studentIds == null) return new ArrayList<>();
//        return studentIds.stream()
//                .filter(Objects::nonNull)
//                .map(id -> {
//                    Student student = new Student();
//                    student.setId(id);
//                    return student;
//                })
//                .collect(Collectors.toList());
//    }
	
    ClassDTO toDTO(ClassEntity classEntity);
    
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "students", ignore = true)
    ClassEntity toEntity(ClassDTO classDTO);

}
