package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Student;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.service.StudentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
//	private final StudentService studentService;

	@Override
	@Transactional
	public Student create(Student student) {
		validateStudent(student);
		return studentRepository.save(student);
	}

	@Override
	public Student getById(Long id) {
		return studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Student", id));
	}

	@Override
	public List<Student> getAllStudent() {
		return studentRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		if (!studentRepository.existsById(id)) {
			throw new ResourceNotFound("Student", id);
		}
		studentRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Student updateStudent(Long id, Student studentUpdate) {
		return studentRepository.findById(id)
				.map(existingStudent -> {
					if (studentUpdate.getFirstName() != null && !studentUpdate.getFirstName().isBlank()) {
						existingStudent.setFirstName(studentUpdate.getFirstName());
					}
					if (studentUpdate.getLastName() != null && !studentUpdate.getLastName().isBlank()) {
						existingStudent.setLastName(studentUpdate.getLastName());
					}
					if (studentUpdate.getDob() != null) {
						existingStudent.setDob(studentUpdate.getDob());
					}
					if (studentUpdate.getGender() != null && !studentUpdate.getGender().isBlank()) {
						existingStudent.setGender(studentUpdate.getGender());
					}
					return studentRepository.save(existingStudent);
				})
				.orElseThrow(() -> new ResourceNotFound("Student", id));
	}

	private void validateStudent(Student student) {
		if (student.getFirstName() == null || student.getFirstName().isBlank()) {
			throw new IllegalArgumentException("First name cannot be null or empty");
		}
		if (student.getLastName() == null || student.getLastName().isBlank()) {
			throw new IllegalArgumentException("Last name cannot be null or empty");
		}
		if (student.getGender() == null || student.getGender().isBlank()) {
			throw new IllegalArgumentException("Gender cannot be null or empty");
		}
		if (student.getDob() == null) {
			throw new IllegalArgumentException("Date of birth cannot be null");
		}
	}




}
