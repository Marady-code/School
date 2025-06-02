package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;

public interface StudentService {

	Student createStudent(Student student);

	StudentDTO getStudentById(Long id);

	List<StudentDTO> getAllStudents();

	void deleteStudent(Long id);

	StudentDTO updateStudent(Long id, Student student);

	List<StudentDTO> getStudentsByClass(String className);

	StudentDTO activateStudent(Long id);

	StudentDTO deactivateStudent(Long id);

}
