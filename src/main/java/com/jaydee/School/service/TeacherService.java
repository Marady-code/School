package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.entity.Teacher;

public interface TeacherService {
	TeacherDTO createTeacher(TeacherDTO teacherDTO);
	TeacherDTO getTeacherById(Long id);
	List<TeacherDTO> getAllTeachers();
	TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO);
	void deleteTeacher(Long id);
	List<TeacherDTO> getTeachersBySubject(String subject);
	TeacherDTO activateTeacher(Long id);
	TeacherDTO deactivateTeacher(Long id);
}
