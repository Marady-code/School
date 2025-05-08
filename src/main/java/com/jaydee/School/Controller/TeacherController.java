package com.jaydee.School.Controller;

import java.util.Collections;
import java.util.List;

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

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.mapper.TeacherMapper;
import com.jaydee.School.service.TeacherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeacherController {

	private final TeacherService teacherService;
	private final TeacherMapper teacherMapper;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
		try {
			Teacher teacher = teacherMapper.toEntity(teacherDTO);
			TeacherDTO createdTeacher = teacherService.createTeacher(teacher);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacher);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
		try {
			TeacherDTO teacher = teacherService.getTeacherById(id);
			return ResponseEntity.ok(teacher);
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<?> getAllTeachers() {
		List<TeacherDTO> teachers = teacherService.getAllTeachers();
		return ResponseEntity.ok(teachers);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO teacherDTO) {
		try {
			Teacher teacher = teacherMapper.toEntity(teacherDTO);
			TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacher);
			return ResponseEntity.ok(updatedTeacher);
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
		try {
			teacherService.deleteTeacher(id);
			return ResponseEntity.ok().body(Collections.singletonMap("message", "Teacher deleted successfully"));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/subject/{subject}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<?> getTeachersBySubject(@PathVariable String subject) {
		List<TeacherDTO> teachers = teacherService.getTeachersBySubject(subject);
		return ResponseEntity.ok(teachers);
	}

	@PutMapping("/{id}/activate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> activateTeacher(@PathVariable Long id) {
		try {
			TeacherDTO teacher = teacherService.activateTeacher(id);
			return ResponseEntity.ok(teacher);
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@PutMapping("/{id}/deactivate")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deactivateTeacher(@PathVariable Long id) {
		try {
			TeacherDTO teacher = teacherService.deactivateTeacher(id);
			return ResponseEntity.ok(teacher);
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}
}
