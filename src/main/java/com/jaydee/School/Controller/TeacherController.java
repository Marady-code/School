package com.jaydee.School.Controller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.mapper.TeacherMapper;
import com.jaydee.School.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teachers")
public class TeacherController {

	private final TeacherService teacherService;
	private final TeacherMapper teacherMapper;

	// @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	@PostMapping
	public ResponseEntity<?> create(@RequestBody TeacherDTO teacherDTO) {
		Teacher teacher = teacherMapper.toEntity(teacherDTO);
		teacher = teacherService.create(teacher);
		return ResponseEntity.ok(teacherMapper.toTeacherDTO(teacher));
	}

	// @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	@GetMapping("{id}")
	public ResponseEntity<?> getByID(@PathVariable Long id) {
		try {
			Teacher teacher = teacherService.getById(id);
			return ResponseEntity.ok(teacherMapper.toTeacherDTO(teacher));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error", e.getMessage()));
		}
	}

	// @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
	@GetMapping
	public ResponseEntity<?> getAllTeacher() {
		List<TeacherDTO> list = teacherService.getAllTeachers().stream()
				.map(teacher -> teacherMapper.toTeacherDTO(teacher)).collect(Collectors.toList());

		return ResponseEntity.ok(list);
	}

	// @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		try {
			teacherService.deleteTeacherById(id);
			return ResponseEntity
					.ok(Collections.singletonMap("message", "Teacher with id = " + id + " deleted succesfully"));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error", e.getMessage()));
		}
	}

	// @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("{id}")
	public ResponseEntity<?> Update(@PathVariable Long id, @RequestBody TeacherDTO teacherDTO) {
		try {
			Teacher teacher = teacherMapper.toEntity(teacherDTO);
			Teacher updateTeacher = teacherService.updateTeacher(id, teacher);
			return ResponseEntity.ok(teacherMapper.toTeacherDTO(updateTeacher));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Error", e.getMessage()));
		}
	}

}
