package com.jaydee.School.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.StudentMapper;
import com.jaydee.School.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;
	private final StudentMapper studentMapper;
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody StudentDTO studentDTO){
		Student student = studentMapper.toEntity(studentDTO);
		student = studentService.create(student);
		return ResponseEntity.ok(studentMapper.toStudentDTO(student));
	}	
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('STUDENT')")
	@GetMapping("{id}")
	public ResponseEntity<?> GetById(@PathVariable Long id){
		try {
			Student student = studentService.getById(id);
			return ResponseEntity.ok(studentMapper.toStudentDTO(student));	
		}catch(ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage() ));
		}
		
	}
	
//	@GetMapping
//	public ResponseEntity<List<Student>> getAllStudents(){
//			List<Student> students = studentService.getAllStudent();
//			if(students.isEmpty()) {
//				return ResponseEntity.noContent().build();
//			}
//			return ResponseEntity.ok(students);
//		
//		
//	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	@GetMapping
	public ResponseEntity<?> getAllStudent(){
		List<StudentDTO> list = studentService.getAllStudent()
			.stream()
			.map(student -> studentMapper.toStudentDTO(student))
			.collect(Collectors.toList());
		
		return ResponseEntity.ok(list);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		try {
	        studentService.deleteById(id);
	        return ResponseEntity.ok(Collections.singletonMap("message", "Student with id = " + id + " deleted successfully."));	
		}catch(ResourceNotFound e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage() ));
		}
				
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("{id}")
	public ResponseEntity<?> Update(@PathVariable Long id, @RequestBody StudentDTO studentDTO){
		try {
			Student student = studentMapper.toEntity(studentDTO);
			Student updateStudent = studentService.updateStudent(id, student);
			return ResponseEntity.ok(studentMapper.toStudentDTO(updateStudent));
		}catch(ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage() ));
		}
		
	}
	
}
