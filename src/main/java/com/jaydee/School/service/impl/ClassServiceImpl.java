package com.jaydee.School.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.ClassDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.mapper.ClassMapper;
import com.jaydee.School.repository.ClassRepository;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.repository.TeacherRepository;
import com.jaydee.School.service.ClassService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClassServiceImpl implements ClassService {

	private final ClassRepository classRepository;
	private final TeacherRepository teacherRepository;
	private final StudentRepository studentRepository;
	private final ClassMapper classMapper;
	
	@Override
	@Transactional
	public ClassEntity create(ClassEntity classEntity) {
		validateClassEntity(classEntity);
		return classRepository.save(classEntity);
	}
	
	@Override
	public List<ClassDTO> getAllClasses() {
		return classRepository.findAll()
				.stream()
				.map(classMapper::toDTO)
				.collect(Collectors.toList());
				
	}
	@Override
	public ClassDTO getClassById(Long id) {
		ClassEntity classEntity = classRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Class", id));
		return classMapper.toDTO(classEntity);
	}
	
	
	@Override
	@Transactional
	public ClassEntity updateClass(Long id, ClassDTO classDTO) {
		ClassEntity existingClass = classRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Class", id));

		// Update basic class information
		if (classDTO.getClassName() != null && !classDTO.getClassName().isBlank()) {
			existingClass.setClassName(classDTO.getClassName());
		}
		if (classDTO.getAcademicYear() != null) {
			existingClass.setAcademicYear(classDTO.getAcademicYear());
		}
		if (classDTO.getTerm() != null) {
			existingClass.setTerm(classDTO.getTerm());
		}
		if (classDTO.getCapacity() != null) {
			existingClass.setCapacity(classDTO.getCapacity());
		}
		if (classDTO.getRoomNumber() != null) {
			existingClass.setRoomNumber(classDTO.getRoomNumber());
		}
		if (classDTO.getDescription() != null) {
			existingClass.setDescription(classDTO.getDescription());
		}

		// Update teacher if provided
		if (classDTO.getTeacherId() != null) {
			Teacher teacher = teacherRepository.findById(classDTO.getTeacherId())
					.orElseThrow(() -> new ResourceNotFound("Teacher", classDTO.getTeacherId()));
			existingClass.setTeacher(teacher);
		}

		// Update students if provided
		if (classDTO.getStudentIds() != null && !classDTO.getStudentIds().isEmpty()) {
			List<Student> students = studentRepository.findAllById(classDTO.getStudentIds());
			if (students.size() != classDTO.getStudentIds().size()) {
				throw new ResourceNotFound("One or more students not found");
			}
			validateClassCapacity(existingClass, students.size());
			existingClass.setStudents(students);
		}

		return classRepository.save(existingClass);
	}

	@Override
	@Transactional
	public void deleteClass(Long id) {
		if (!classRepository.existsById(id)) {
			throw new ResourceNotFound("Class", id);
		}
		classRepository.deleteById(id);
		
	}
	
	@Override
	@Transactional
	public ClassEntity assignTeacherToClass(Long classId, Long teacherId) {
		ClassEntity classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFound("Class", classId));
		
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new ResourceNotFound("Teacher", teacherId));
		
		classEntity.setTeacher(teacher);
		return classRepository.save(classEntity);
	}

	@Override
	@Transactional
	public ClassEntity addStudentToClass(Long classId, Long studentId) {
		ClassEntity classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFound("Class", classId));
		
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFound("Student", studentId));
		
		if (classEntity.getStudents() == null) {
			classEntity.setStudents(new ArrayList<>());
		}
		
		if (!classEntity.getStudents().contains(student)) {
			validateClassCapacity(classEntity, classEntity.getStudents().size() + 1);
			classEntity.getStudents().add(student);
			student.setClassEntity(classEntity);
			studentRepository.save(student);
		}
		
		return classRepository.save(classEntity);
	}

	@Override
	@Transactional
	public ClassEntity removeStudentFromClass(Long classId, Long studentId) {
		ClassEntity classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFound("Class", classId));
		
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFound("Student", studentId));
		
		if (classEntity.getStudents() != null && classEntity.getStudents().contains(student)) {
			classEntity.getStudents().remove(student);
			student.setClassEntity(null);
			studentRepository.save(student);
		}
		
		return classRepository.save(classEntity);
	}

	private void validateClassEntity(ClassEntity classEntity) {
		if (classEntity.getClassName() == null || classEntity.getClassName().isBlank()) {
			throw new IllegalArgumentException("Class name cannot be null or empty");
		}
		if (classEntity.getAcademicYear() == null || classEntity.getAcademicYear().isBlank()) {
			throw new IllegalArgumentException("Academic year cannot be null or empty");
		}
		if (classEntity.getTerm() == null || classEntity.getTerm().isBlank()) {
			throw new IllegalArgumentException("Term cannot be null or empty");
		}
		if (classEntity.getCapacity() != null && classEntity.getCapacity() <= 0) {
			throw new IllegalArgumentException("Class capacity must be greater than 0");
		}
	}

	private void validateClassCapacity(ClassEntity classEntity, int newStudentCount) {
		if (classEntity.getCapacity() != null && newStudentCount > classEntity.getCapacity()) {
			throw new IllegalArgumentException("Class capacity exceeded. Maximum capacity is: " + classEntity.getCapacity());
		}
	}

}
