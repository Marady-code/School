package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Student;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.service.StudentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService{

	
	private final StudentRepository studentRepository;
//	private final StudentService studentService;

	@Override
	public Student create(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public Student getById(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound(id));
	}

	@Override
	public List<Student> getAllStudent() {
		return studentRepository.findAll();
	}

	@Override
	public void deleteById(Long id) {
		if(!studentRepository.existsById(id)) {
			throw new ResourceNotFound(id);
		}
			studentRepository.deleteById(id);
	}

	@Override
	public Student updateStudent(Student student) {
		getById(student.getId());
		return studentRepository.save(student);
	}




}
