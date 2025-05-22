package com.jaydee.School.service;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

import java.util.List;

public interface StudentService {

	StudentDTO createStudent(Student student);	StudentDTO getStudentById(Long id);
	List<StudentDTO> getAllStudents();
	void deleteStudent(Long id);
	StudentDTO updateStudent(Long id, Student student);
	List<StudentDTO> getStudentsByClass(String className);
	StudentDTO activateStudent(Long id);
	StudentDTO deactivateStudent(Long id);

}
