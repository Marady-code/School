package com.jaydee.School.Controller;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.TimeTableDTO;
import com.jaydee.School.entity.ClassEntity;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.service.TimeTableService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/time_tables")
public class TimeTableController {

	@Autowired
	private TimeTableService timeTableService;

	@PostMapping
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<TimeTableDTO> createTimeTable(@Valid @RequestBody TimeTableDTO timeTableDTO) {
		return ResponseEntity.ok(timeTableService.createTimeTable(timeTableDTO));
	}

	@PostMapping("/bulk")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<TimeTableDTO>> createBulkTimeTable(
			@Valid @RequestBody List<TimeTableDTO> timeTableDTOs) {
		return ResponseEntity.ok(timeTableService.createBulkTimeTable(timeTableDTOs));
	}

	@PutMapping("/{id}")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<TimeTableDTO> updateTimeTable(@PathVariable Long id,
			@Valid @RequestBody TimeTableDTO timeTableDTO) {
		return ResponseEntity.ok(timeTableService.updateTimeTable(id, timeTableDTO));
	}

	@GetMapping("/class/{classId}")
	// @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
	public ResponseEntity<List<TimeTableDTO>> getClassTimeTable(@PathVariable Long classId) {
		ClassEntity classEntity = new ClassEntity();
		classEntity.setId(classId);
		return ResponseEntity.ok(timeTableService.getClassTimeTable(classEntity));
	}

	@GetMapping("/teacher/{teacherId}")
	// @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<List<TimeTableDTO>> getTeacherTimeTable(@PathVariable Long teacherId) {
		Teacher teacher = new Teacher();
		teacher.setId(teacherId);
		return ResponseEntity.ok(timeTableService.getTeacherTimeTable(teacher));
	}

	@GetMapping("/class/{classId}/day/{dayOfWeek}")
	// @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
	public ResponseEntity<List<TimeTableDTO>> getClassDayTimeTable(@PathVariable Long classId,
			@PathVariable DayOfWeek dayOfWeek) {
		ClassEntity classEntity = new ClassEntity();
		classEntity.setId(classId);
		return ResponseEntity.ok(timeTableService.getClassDayTimeTable(classEntity, dayOfWeek));
	}

	@GetMapping("/subject/{subjectId}")
	// @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
	public ResponseEntity<List<TimeTableDTO>> getTimeTableBySubject(@PathVariable Long subjectId) {
		return ResponseEntity.ok(timeTableService.getTimeTableBySubject(subjectId));
	}

	@GetMapping("/academic-year/{academicYear}/term/{term}")
	// @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT', 'PARENT')")
	public ResponseEntity<List<TimeTableDTO>> getTimeTableByAcademicYearAndTerm(@PathVariable String academicYear,
			@PathVariable String term) {
		return ResponseEntity.ok(timeTableService.getTimeTableByAcademicYearAndTerm(academicYear, term));
	}

	@DeleteMapping("/{id}")
	// @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id) {
		timeTableService.deleteTimeTable(id);
		return ResponseEntity.ok().build();
	}
}