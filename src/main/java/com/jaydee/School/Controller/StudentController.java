package com.jaydee.School.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.StudentMapper;
import com.jaydee.School.service.StudentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

	private final StudentService studentService;
	private final StudentMapper studentMapper;
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody StudentDTO studentDTO){
		Student student = studentMapper.toEntity(studentDTO);
		student = studentService.create(student);
		return ResponseEntity.ok(studentMapper.toStudentDTO(student));
	}

	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long StudentId){
		Student student = studentService.getById(StudentId);
		return ResponseEntity.ok(studentMapper.toStudentDTO(student));
	}
}
