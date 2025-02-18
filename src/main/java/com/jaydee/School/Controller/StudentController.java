package com.jaydee.School.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.StudentMapper;
import com.jaydee.School.service.StudentService;

@RestController
@RequestMapping("students")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody StudentDTO studentDTO){
		Student student = studentMapper.toEntity(studentDTO);
		student = studentService.create(student);
		return ResponseEntity.ok(studentMapper.toDTO(student));
	}
	
//	public ResponseEntity<?> getById(@PathVariable("id") Long StudentId){
//		Student student = studentService.getById(StudentId);
//		return ResponseEntity.ok(studentMapper.toDTO(student));
//	}
}
