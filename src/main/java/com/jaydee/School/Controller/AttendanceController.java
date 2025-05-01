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

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Attendance;
import com.jaydee.School.mapper.AttendanceMapper;
import com.jaydee.School.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendances")
public class AttendanceController {

	private final AttendanceService attendanceService;
	private final AttendanceMapper attendanceMapper;

	// @PreAuthorize("hasAuthority('attendance:read','attendance:write') or
	// hasRole('ADMIN') or hasRole('TEACHER')")
	@GetMapping
	public ResponseEntity<?> getAllAttendances() {
		List<AttendanceDTO> attendances = attendanceService.getAllAttendances();
		return ResponseEntity.ok(attendances);
	}

	// @PreAuthorize("hasAuthority('attendance:read','attendance:write') or
	// hasRole('ADMIN') or hasRole('TEACHER')")
	@PostMapping
	public ResponseEntity<AttendanceDTO> markAttendace(@RequestBody AttendanceDTO attendanceDTO) {
		AttendanceDTO savedAttendance = attendanceService.markAttendance(attendanceDTO);
		return new ResponseEntity<>(savedAttendance, HttpStatus.CREATED);
	}

	// @PreAuthorize("hasAuthority('attendance:read') or hasRole('ADMIN') or
	// hasRole('TEACHER') or hasRole('STUDENT')")
	@GetMapping("/students/{studentId}")
	public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentId(@PathVariable Long studentId) {
		try {
			List<AttendanceDTO> attendances = attendanceService.getAttendanceByStudentId(studentId);
			return ResponseEntity.ok(attendances);
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

	}

//	@GetMapping("/teachers/{teacherId}")
//	public ResponseEntity<List<AttendanceDTO>>  getAttendanceByTeacher(@PathVariable Long teacherId){
//		List<AttendanceDTO> attendances = attendanceService.getAttendanceByTeacherId(teacherId);
//		return ResponseEntity.ok(attendances);
//	}

	// @PreAuthorize("hasAuthority('attendance:read','attendance:write') or
	// hasRole('ADMIN') or hasRole('TEACHER')")
	@PutMapping("{studentId}")
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
	public ResponseEntity<?> DeleteAttendanceByStudentId(@PathVariable Long id) {
		try {
			attendanceService.deleteAttendanceByStudentId(id);
			return ResponseEntity
					.ok(Collections.singletonMap("message", "Student with id = " + id + "deleted successfullt."));
		} catch (ResourceNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

//	public ResponseEntity<?> getById(@PathVariable("id") Long studentId){
//		try {
//			List<Attendance> attendance = attendanceService.getAttendanceByStudentId(studentId);
//			return ResponseEntity.ok(attendanceMapper.toDTO(attendance));
//		}catch(ResourceNotFound e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage() ));
//		}
//		
//	}
}
