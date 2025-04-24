package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.entity.Teacher;

public interface TeacherService {
	Teacher create(Teacher teacher);
	Teacher getById(Long id);
	List<Teacher> getAllTeachers();
	void deleteTeacherById(Long id);
	Teacher updateTeacher(Long id, Teacher teacherUpdate);
}
