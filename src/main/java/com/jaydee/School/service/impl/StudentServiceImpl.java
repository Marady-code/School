package com.jaydee.School.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.StudentMapper;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;

	@Override
	public StudentDTO createStudent(Student student) {
		Student savedStudent = studentRepository.save(student);
		return studentMapper.toStudentDTO(savedStudent);
	}
	@Override
	public StudentDTO getStudentById(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));
		return studentMapper.toStudentDTO(student);
	}

	@Override
	public List<StudentDTO> getAllStudents() {
		return studentRepository.findAll().stream().map(studentMapper::toStudentDTO).collect(Collectors.toList());
	}
	@Override
	public StudentDTO updateStudent(Long id, Student student) {
		Student existingStudent = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));

		studentMapper.updateEntityFromDTO(studentMapper.toStudentDTO(student), existingStudent);
		Student updatedStudent = studentRepository.save(existingStudent);
		return studentMapper.toStudentDTO(updatedStudent);
	}
	@Override
	public void deleteStudent(Long id) {
		if (!studentRepository.existsById(id)) {
			throw new ResourceNotFound("Student", id);
		}
		studentRepository.deleteById(id);
	}

	@Override
	public List<StudentDTO> getStudentsByClass(String className) {
		return studentRepository.findByClassEntity_ClassName(className).stream().map(studentMapper::toStudentDTO)
				.collect(Collectors.toList());
	}
	@Override
	public StudentDTO activateStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));
		student.setIsActive(true);
		Student updatedStudent = studentRepository.save(student);
		return studentMapper.toStudentDTO(updatedStudent);
	}
	@Override
	public StudentDTO deactivateStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));
		student.setIsActive(false);
		Student updatedStudent = studentRepository.save(student);
		return studentMapper.toStudentDTO(updatedStudent);
	}

	private void validateStudent(Student student) {
		if (student.getFirstName() == null || student.getFirstName().isBlank()) {
			throw new IllegalArgumentException("First name cannot be null or empty");
		}
		if (student.getLastName() == null || student.getLastName().isBlank()) {
			throw new IllegalArgumentException("Last name cannot be null or empty");
		}
		if (student.getGender() == null) {
			throw new IllegalArgumentException("Gender cannot be null");
		}
		if (student.getDob() == null) {
			throw new IllegalArgumentException("Date of birth cannot be null");
		}
	}
}
