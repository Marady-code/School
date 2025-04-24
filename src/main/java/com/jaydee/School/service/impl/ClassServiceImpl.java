package com.jaydee.School.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
	public ClassEntity create(ClassEntity classEntity) {
	    if (classEntity.getClassName() == null || classEntity.getClassName().isBlank()) {
	        throw new IllegalArgumentException("Class name cannot be null or empty");
	    }
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
				.orElseThrow(() -> new RuntimeException("Class not found!"));
		return classMapper.toDTO(classEntity);
	}
	
	
//	@Override
//	public ClassEntity updateClass(Long id, ClassDTO classDTO) {
//		ClassEntity existingClass = classRepository.findById(id)
//	            .orElseThrow(() -> new ResourceNotFound("Class not found with id: ", id));
//	    
//	    // Update fields
//	    existingClass.setClassName(classDTO.getClassName());
//	    
//	    // Update teacher if changed
//	    if (classDTO.getTeacherId() != null && 
//	        !classDTO.getTeacherId().equals(existingClass.getTeacher().getId())) {
//	        Teacher teacher = teacherRepository.findById(classDTO.getTeacherId())
//	                .orElseThrow(() -> new ResourceNotFound("Teacher not found with id: ", classDTO.getTeacherId()));
//	        existingClass.setTeacher(teacher);
//	    }
//	    
//	    // Update students if changed
//	    if (classDTO.getStudentIds() != null) {
//	        List<Student> students = studentRepository.findAllById(classDTO.getStudentIds());
//	        if (students.size() != classDTO.getStudentIds().size()) {
//	            throw new ResourceNotFoundException("One or more students not found");
//	        }
//	        existingClass.setStudents(students);
//	        
//	        // Update bidirectional relationship
//	        for (Student student : students) {
//	            student.setClassEntity(existingClass);
//	        }
//	    }
//	    
//	    return classRepository.save(existingClass);
//	}
	
	@Override
	public ClassEntity updateClass(Long id, ClassDTO classDTO) {
	    ClassEntity existingClass = classRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFound("Class not found with id: ", id));

	    // Update className only
	    existingClass.setClassName(classDTO.getClassName());

	    // Save and return the updated class
	    return classRepository.save(existingClass);
	}

	@Override
	public void deleteClass(Long id) {
		if(!classRepository.existsById(id)) {
			throw new ResourceNotFound("Class", id);
		}
		classRepository.deleteById(id);
		
	}
	
	@Override
	public ClassEntity assignTeacherToClass(Long classId, Long teacherId) {
	    ClassEntity classEntity = classRepository.findById(classId)
	            .orElseThrow(() -> new ResourceNotFound("Class not found", classId));
	    
	    Teacher teacher = teacherRepository.findById(teacherId)
	            .orElseThrow(() -> new ResourceNotFound("Teacher not found", teacherId));
	    
	    classEntity.setTeacher(teacher);
	    return classRepository.save(classEntity);
	}

	@Override
	public ClassEntity addStudentToClass(Long classId, Long studentId) {
	    ClassEntity classEntity = classRepository.findById(classId)
	            .orElseThrow(() -> new ResourceNotFound("Class not found", classId));
	    
	    Student student = studentRepository.findById(studentId)
	            .orElseThrow(() -> new ResourceNotFound("Student not found", studentId));
	    
	    if (classEntity.getStudents() == null) {
	        classEntity.setStudents(new ArrayList<>());
	    }
	    
	    if (!classEntity.getStudents().contains(student)) {
	        classEntity.getStudents().add(student);
	        student.setClassEntity(classEntity);
	        studentRepository.save(student);
	    }
	    
	    return classRepository.save(classEntity);
	}

	@Override
	public ClassEntity removeStudentFromClass(Long classId, Long studentId) {
	    ClassEntity classEntity = classRepository.findById(classId)
	            .orElseThrow(() -> new ResourceNotFound("Class not found", classId));
	    
	    Student student = studentRepository.findById(studentId)
	            .orElseThrow(() -> new ResourceNotFound("Student not found", studentId));
	    
	    if (classEntity.getStudents() != null && classEntity.getStudents().contains(student)) {
	        classEntity.getStudents().remove(student);
	        student.setClassEntity(null);
	        studentRepository.save(student);
	    }
	    
	    return classRepository.save(classEntity);
	}

}
