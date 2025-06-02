package com.jaydee.School.service;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.entity.Teacher;

import java.util.List;

public interface TeacherService {
	TeacherDTO createTeacher(Teacher teacher);	TeacherDTO getTeacherById(Long id);
	List<TeacherDTO> getAllTeachers();
	TeacherDTO updateTeacher(Long id, Teacher teacher);
	void deleteTeacher(Long id);
	List<TeacherDTO> getTeachersBySubject(String subject);
	TeacherDTO activateTeacher(Long id);
	TeacherDTO deactivateTeacher(Long id);
}
