package com.jaydee.School.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jaydee.School.entity.Attendance;

public interface AttendaceRepository extends JpaRepository<Attendance, Long>{
	List<Attendance> findByStudentId(Long studentId);
	//List<Attendance> findByTeacherId(Long techerId);
}
