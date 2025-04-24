package com.jaydee.School.service.impl;

import java.util.List;

import org.apache.hadoop.yarn.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.jaydee.School.DTO.AttendanceDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Attendance;
import com.jaydee.School.entity.Student;
import com.jaydee.School.mapper.AttendanceMapper;
import com.jaydee.School.repository.AttendaceRepository;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.repository.TeacherRepository;
import com.jaydee.School.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AttendanceServiceImpl implements AttendanceService{

	
	private final AttendaceRepository attendaceRepository;
	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final AttendanceMapper attendanceMapper;
	
	@Override
	public AttendanceDTO markAttendance(AttendanceDTO attendanceDTO) {
		
		Attendance attendance = attendanceMapper.toEntity(attendanceDTO);
	    // Fetch Student
	    Student student = studentRepository.findById(attendance.getStudent().getId())
	    		.orElseThrow(() -> new ResourceNotFound("Student", attendanceDTO.getStudentId()));
	    // Fetch Teacher
//	    Teacher teacher = teacherRepository.findById(attendance.getTeacher().getId())
//	            .orElseThrow(() -> new ResourceNotFound("Teacher", attendance.getTeacher().getId()));
//	    		.orElseThrow(() -> new ResourceNotFound("Teacher", attendanceDTO.getTeacherId()));

	    // Set the retrieved student and teacher objects
	    attendance.setStudent(student);
//	    attendance.setTeacher(teacher);

	    Attendance savedAttendance = attendaceRepository.save(attendance);
	    // Save the attendance record
	    return attendanceMapper.toDTO(savedAttendance);
	}


	@Override
	public List<AttendanceDTO> getAllAttendances() {
		List<Attendance> attendances = attendaceRepository.findAll();
		return attendanceMapper.toDTOList(attendances);
	}

	@Override
	public List<AttendanceDTO> getAttendanceByStudentId(Long studentId) {
		studentRepository.findById(studentId)
		.orElseThrow(() -> new ResourceNotFound("Student", studentId));
		
		List<Attendance> attendances = attendaceRepository.findByStudentId(studentId);
		if(attendances.isEmpty()) {
			throw new ResourceNotFoundException("No attendance records found for Student ID : " + studentId);
		}
		return attendanceMapper.toDTOList(attendances);
	}

//	@Override
//	public List<AttendanceDTO> getAttendanceByTeacherId(Long teacherId) {
//		teacherRepository.findById(teacherId)
//		.orElseThrow(() -> new ResourceNotFound("Teacher", teacherId));
//		List<Attendance> attendances = attendaceRepository.findByTeacherId(teacherId);
//		if(attendances.isEmpty()) {
//			throw new ResourceNotFoundException("No attendance records found for Teacher ID : " + teacherId);
//		}
//		return attendanceMapper.toDTOList(attendances);
//	}


	@Override
	public Attendance updateAttendance(Long studentId, Attendance attendanceUpdate) {
		return attendaceRepository.findById(studentId)
				.map(existingStudent -> {
					existingStudent.setStatus(attendanceUpdate.getStatus());
					existingStudent.setDate(attendanceUpdate.getDate());
					return attendaceRepository.save(existingStudent);
				})
				.orElseThrow(() -> new ResourceNotFound("Student", studentId));
				
	}


	@Override
	public void deleteAttendanceByStudentId(Long studentId) {
		if(!attendaceRepository.existsById(studentId)) {
			throw new ResourceNotFound("Attendance Student with id :", studentId);
		}
			attendaceRepository.deleteById(studentId);
		
	}
	




	

}
