package com.jaydee.School.Controller;

import java.util.Collections;
import java.util.List;

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

import com.jaydee.School.DTO.ClassDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.service.ClassService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

	private final ClassService classService;
	
	@PostMapping
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createClass(@Valid @RequestBody ClassEntity classEntity) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(classService.create(classEntity));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}
	
	@GetMapping("/{id}")
	//@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	public ResponseEntity<?> getClassById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(classService.getClassById(id));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}
	
	@DeleteMapping("/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteClass(@PathVariable Long id) {
		try {
			classService.deleteClass(id);
			return ResponseEntity.ok().build();
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}
	
	@PutMapping("/{id}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateClass(
			@PathVariable Long id,
			@Valid @RequestBody ClassDTO classDTO) {
		try {
			return ResponseEntity.ok(classService.updateClass(id, classDTO));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}
	
	@PostMapping("/{classId}/teacher/{teacherId}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> assignTeacher(
			@PathVariable Long classId,
			@PathVariable Long teacherId) {
		try {
			return ResponseEntity.ok(classService.assignTeacherToClass(classId, teacherId));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@PostMapping("/{classId}/student/{studentId}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addStudent(
			@PathVariable Long classId,
			@PathVariable Long studentId) {
		try {
			return ResponseEntity.ok(classService.addStudentToClass(classId, studentId));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@DeleteMapping("/{classId}/student/{studentId}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> removeStudent(
			@PathVariable Long classId,
			@PathVariable Long studentId) {
		try {
			return ResponseEntity.ok(classService.removeStudentFromClass(classId, studentId));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}
	
	@GetMapping
	//@PreAuthorize("hasAuthority('class:read') or hasRole('ADMIN') or hasRole('TEACHER')")
	public ResponseEntity<?> getAllClasses() {
		List<ClassDTO> list = classService.getAllClasses();
		return ResponseEntity.ok(list);
	}
}
