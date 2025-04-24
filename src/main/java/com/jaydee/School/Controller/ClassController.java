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
import com.jaydee.School.mapper.ClassMapper;
import com.jaydee.School.service.ClassService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/classes")
public class ClassController {
	
	private final ClassService classService;
	private final ClassMapper classMapper;
	
//	@PostMapping
//	public ResponseEntity<?> create(@RequestBody ClassDTO classDTO){
//		ClassEntity classEntity = classMapper.toEntity(classDTO);
//		classEntity = classService.createClass(classEntity);
//		return ResponseEntity.ok(classMapper.toDTO(classEntity));
//	}	
	
	
	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody ClassDTO classDTO) {
	    ClassEntity classEntity = classMapper.toEntity(classDTO);
	    classEntity = classService.create(classEntity);
	    return ResponseEntity.ok(classMapper.toDTO(classEntity));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getClass(@PathVariable Long id){
		try {
			return ResponseEntity.ok(classService.getClassById(id));
		}catch(ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage() ));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteClass(@PathVariable Long id){
		try {
			classService.deleteClass(id);
			return ResponseEntity.noContent().build();
		}catch(ResourceNotFound e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage() ));
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateClass(@PathVariable Long id, @Valid @RequestBody ClassDTO classDTO) {
	    try {
	        return ResponseEntity.ok(classService.updateClass(id, classDTO));
	    } catch (ResourceNotFound e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Collections.singletonMap("error", e.getMessage()));
	    }
	}
	
	@PostMapping("/{classId}/teacher/{teacherId}")
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
	public ResponseEntity<?> GetAllClasses(){
		List<ClassDTO> list = classService.getAllClasses();
		return ResponseEntity.ok(list);
	}
	
	
	
	
	
}
