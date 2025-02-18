package com.jaydee.School.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaydee.School.entity.Student;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public Student create(Student student) {
		return studentRepository.save(student);
	}

//	@Override
//	public Student getById(Long id) {
//		return studentRepository.findById(id);
//	}

}
