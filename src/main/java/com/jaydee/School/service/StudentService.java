package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.DTO.StudentRegistrationDTO;
import com.jaydee.School.entity.Student;

public interface StudentService {

	StudentDTO registerStudent(StudentRegistrationDTO registrationDTO);

	Student createStudent(Student student);

	StudentDTO getStudentById(Long id);

	List<StudentDTO> getAllStudents();

	void deleteStudent(Long id);

	StudentDTO updateStudent(Long id, StudentDTO studentDTO);

	List<StudentDTO> getStudentsByClass(String className);

	StudentDTO activateStudent(Long id);

	StudentDTO deactivateStudent(Long id);

}
