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

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Attendance;
import com.jaydee.School.mapper.AttendanceMapper;
import com.jaydee.School.service.AttendanceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

	private final AttendanceService attendanceService;
	private final AttendanceMapper attendanceMapper;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<AttendanceDTO> markAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
		return ResponseEntity.ok(attendanceService.markAttendance(attendanceDTO));
	}

	@GetMapping("/student/{studentId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable Long studentId) {
		try {
			List<AttendanceDTO> attendances = attendanceService.getAttendanceByStudentId(studentId);
			return ResponseEntity.ok(attendances);
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<?> UpdateAttendance(@PathVariable Long studentId, @RequestBody AttendanceDTO attendanceDTO) {
		try {
			Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
			Attendance updateAttendance = attendanceService.updateAttendance(studentId, attendance);
			return ResponseEntity.ok(attendanceMapper.toDTO(updateAttendance));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

	@DeleteMapping("{studentId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> DeleteAttendanceByStudentId(@PathVariable Long id) {
		try {
			attendanceService.deleteAttendanceByStudentId(id);
			return ResponseEntity
					.ok(Collections.singletonMap("message", "Student with id = " + id + "deleted successfullt."));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

}
