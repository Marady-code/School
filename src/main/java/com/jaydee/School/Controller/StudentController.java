package com.jaydee.School.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.StudentMapper;
import com.jaydee.School.service.StudentService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;
	private final StudentMapper studentMapper;
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody StudentDTO studentDTO){
		Student student = studentMapper.toEntity(studentDTO);
		student = studentService.create(student);
		return ResponseEntity.ok(studentMapper.toStudentDTO(student));
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id){
		Student student = studentService.getById(id);
		return ResponseEntity.ok(studentMapper.toStudentDTO(student));
	}
	
	@GetMapping
	public ResponseEntity<List<Student>> getAllStudents(){
		List<Student> students = studentService.getAllStudent();
		if(students.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(students);
	}
	
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
		try {
			studentService.deleteById(id);
			return ResponseEntity.ok("Student with id :" + id + "deleted successfully.");
		}catch(EntityNotFoundException e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Error : Student with id :\" + id + Not found!");
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting the students");
		}
	}
}
