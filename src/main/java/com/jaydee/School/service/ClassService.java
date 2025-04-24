package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.ClassDTO;
import com.jaydee.School.entity.ClassEntity;

public interface ClassService {

	ClassEntity create(ClassEntity classEntity);
	
	//ClassDTO createClass(ClassDTO classDTO);
	
	List<ClassDTO> getAllClasses();
	
	ClassDTO getClassById(Long id);
	
	ClassEntity updateClass(Long id, ClassDTO classDTO);
	
	void deleteClass(Long id);
	
    ClassEntity assignTeacherToClass(Long classId, Long teacherId);
    
    ClassEntity addStudentToClass(Long classId, Long studentId);
    
    ClassEntity removeStudentFromClass(Long classId, Long studentId);
    
}
