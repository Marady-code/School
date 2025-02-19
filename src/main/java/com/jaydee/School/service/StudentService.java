package com.jaydee.School.service;

import com.jaydee.School.entity.Student;

import java.util.List;

public interface StudentService {
	
	Student create(Student student);
	Student getById(Long id);
	List<Student> getAllStudent();
	void deleteStudentById(Long id);
	Student updateStudent(Student student);




}
