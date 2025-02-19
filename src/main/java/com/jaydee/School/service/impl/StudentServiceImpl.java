package com.jaydee.School.service.impl;

import com.jaydee.School.Exception.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaydee.School.entity.Student;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.service.StudentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{

	private final StudentRepository studentRepository;

	@Override
	public Student create(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public Student getById(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Student",id));
	}

	@Override
	public List<Student> getAllStudent() {
		return List.of();
	}

	@Override
	public void deleteStudentById(Long id) {

	}

	@Override
	public Student updateStudent(Student student) {
		return null;
	}



}
