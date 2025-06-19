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
	public ClassDTO create(ClassDTO classDTO) {
		ClassEntity classEntity = classMapper.toEntity(classDTO);
		validateClassEntity(classEntity);
		ClassEntity savedClass = classRepository.save(classEntity);
		return classMapper.toDTO(savedClass);
	}

	@Override
	public List<ClassDTO> getAllClasses() {
		return classRepository.findAll().stream()
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
	public ClassDTO updateClass(Long id, ClassDTO classDTO) {
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

		ClassEntity updatedClass = classRepository.save(existingClass);
		return classMapper.toDTO(updatedClass);
	}

	@Override
	@Transactional
	public void deleteClass(Long id) {
		ClassEntity classEntity = classRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Class", id));

		// Force fetch students as a managed list
		List<Student> students = new ArrayList<>(classEntity.getStudents());
		for (Student student : students) {
			student.setClassEntity(null);
			studentRepository.save(student);
		}

		// Now delete the class
		classRepository.delete(classEntity);
	}

	@Override
	@Transactional
	public ClassDTO assignTeacherToClass(Long classId, Long teacherId) {
		ClassEntity classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFound("Class", classId));
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new ResourceNotFound("Teacher", teacherId));
		
		classEntity.setTeacher(teacher);
		ClassEntity updatedClass = classRepository.save(classEntity);
		return classMapper.toDTO(updatedClass);
	}

	@Override
	@Transactional
	public ClassDTO addStudentToClass(Long classId, Long studentId) {
		ClassEntity classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFound("Class", classId));
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFound("Student", studentId));
		
		// Check if student is already in the class
		if (classEntity.getStudents() != null && classEntity.getStudents().contains(student)) {
			return classMapper.toDTO(classEntity);
		}
		
		// Initialize students list if null
		if (classEntity.getStudents() == null) {
			classEntity.setStudents(new ArrayList<>());
		}
		
		// Validate class capacity
		validateClassCapacity(classEntity, classEntity.getStudents().size() + 1);
		
		// Add student to class and update the bidirectional relationship
		classEntity.getStudents().add(student);
		student.setClassEntity(classEntity);
		
		// Save both entities to maintain the relationship
		studentRepository.save(student);
		ClassEntity updatedClass = classRepository.save(classEntity);
		
		return classMapper.toDTO(updatedClass);
	}

	@Override
	@Transactional
	public ClassDTO removeStudentFromClass(Long classId, Long studentId) {
		ClassEntity classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFound("Class", classId));
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFound("Student", studentId));
		
		if (classEntity.getStudents() != null) {
			// Remove student from class
			boolean removed = classEntity.getStudents().removeIf(s -> s.getId().equals(studentId));
			if (removed) {
				// Update the student's class reference
				student.setClassEntity(null);
				studentRepository.save(student);
			}
		}
		
		ClassEntity updatedClass = classRepository.save(classEntity);
		return classMapper.toDTO(updatedClass);
	}

	private void validateClassEntity(ClassEntity classEntity) {
		if (classEntity.getClassName() == null || classEntity.getClassName().isBlank()) {
			throw new IllegalArgumentException("Class name is required");
		}
		if (classEntity.getCapacity() != null && classEntity.getCapacity() <= 0) {
			throw new IllegalArgumentException("Class capacity must be greater than 0");
		}
	}

	private void validateClassCapacity(ClassEntity classEntity, int studentCount) {
		if (classEntity.getCapacity() != null && studentCount > classEntity.getCapacity()) {
			throw new IllegalArgumentException("Class capacity exceeded");
		}
	}
}
