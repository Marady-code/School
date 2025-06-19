package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.ClassDTO;
import com.jaydee.School.entity.ClassEntity;

public interface ClassService {

	ClassDTO create(ClassDTO classDTO);
	
	//ClassDTO createClass(ClassDTO classDTO);
	
	List<ClassDTO> getAllClasses();
	
	ClassDTO getClassById(Long id);
	
	ClassDTO updateClass(Long id, ClassDTO classDTO);
	
	void deleteClass(Long id);
	
    ClassDTO assignTeacherToClass(Long classId, Long teacherId);
    
    ClassDTO addStudentToClass(Long classId, Long studentId);
    
    ClassDTO removeStudentFromClass(Long classId, Long studentId);
    
}
