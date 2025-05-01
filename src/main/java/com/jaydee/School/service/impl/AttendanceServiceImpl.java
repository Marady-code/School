package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Attendance;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.AttendanceMapper;
import com.jaydee.School.repository.AttendaceRepository;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendaceRepository attendaceRepository;
	private final StudentRepository studentRepository;
	private final AttendanceMapper attendanceMapper;

	@Override
	@Transactional
	public AttendanceDTO markAttendance(AttendanceDTO attendanceDTO) {
		validateAttendanceDTO(attendanceDTO);
		
		Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
		Student student = studentRepository.findById(attendanceDTO.getStudentId())
				.orElseThrow(() -> new ResourceNotFound("Student", attendanceDTO.getStudentId()));

		attendance.setStudent(student);
		Attendance savedAttendance = attendaceRepository.save(attendance);
		return attendanceMapper.toDTO(savedAttendance);
	}

	@Override
	public List<AttendanceDTO> getAllAttendances() {
		List<Attendance> attendances = attendaceRepository.findAll();
		return attendanceMapper.toDTOList(attendances);
	}

	@Override
	public List<AttendanceDTO> getAttendanceByStudentId(Long studentId) {
		if (!studentRepository.existsById(studentId)) {
			throw new ResourceNotFound("Student", studentId);
		}
		
		List<Attendance> attendances = attendaceRepository.findByStudentId(studentId);
		if (attendances.isEmpty()) {
			throw new ResourceNotFound("No attendance records found for student");
		}
		return attendanceMapper.toDTOList(attendances);
	}

	@Override
	public Attendance updateAttendance(Long studentId, Attendance attendanceUpdate) {
		if (!studentRepository.existsById(studentId)) {
			throw new ResourceNotFound("Student", studentId);
		}

		return attendaceRepository.findById(studentId)
				.map(existingAttendance -> {
					if (attendanceUpdate.getStatus() != null) {
						existingAttendance.setStatus(attendanceUpdate.getStatus());
					}
					if (attendanceUpdate.getDate() != null) {
						existingAttendance.setDate(attendanceUpdate.getDate());
					}
					return attendaceRepository.save(existingAttendance);
				})
				.orElseThrow(() -> new ResourceNotFound("Attendance", studentId));
	}

	@Override
	@Transactional
	public void deleteAttendanceByStudentId(Long studentId) {
		if (!studentRepository.existsById(studentId)) {
			throw new ResourceNotFound("Student", studentId);
		}
		if (!attendaceRepository.existsById(studentId)) {
			throw new ResourceNotFound("Attendance", studentId);
		}
		attendaceRepository.deleteById(studentId);
	}

	private void validateAttendanceDTO(AttendanceDTO attendanceDTO) {
		if (attendanceDTO.getStudentId() == null) {
			throw new IllegalArgumentException("Student ID cannot be null");
		}
		if (attendanceDTO.getStatus() == null) {
			throw new IllegalArgumentException("Attendance status cannot be null");
		}
		if (attendanceDTO.getDate() == null) {
			throw new IllegalArgumentException("Attendance date cannot be null");
		}
	}
}
