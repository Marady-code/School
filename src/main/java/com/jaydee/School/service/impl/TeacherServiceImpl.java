package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.repository.TeacherRepository;
import com.jaydee.School.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService{

	private final TeacherRepository teacherRepository;

	@Override
	public Teacher create(Teacher teacher) {
		return teacherRepository.save(teacher);
	}

	@Override
	public Teacher getById(Long id) {
		return teacherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Teacher", id));
	}

	@Override
	public List<Teacher> getAllTeachers() {
		return teacherRepository.findAll();
	}

	@Override
	public void deleteTeacherById(Long id) {
		if(!teacherRepository.existsById(id)) {
			throw new ResourceNotFound("Teacher", id);
		}
			teacherRepository.deleteById(id);
		
	}

	@Override
	@Transactional
	public Teacher updateTeacher(Long id, Teacher teacherUpdate) {
		return teacherRepository.findById(id)
				.map(existingTeacher -> {
					existingTeacher.setName(teacherUpdate.getName());
					existingTeacher.setDob(teacherUpdate.getDob());
					existingTeacher.setPhoneNumber(teacherUpdate.getPhoneNumber());
					existingTeacher.setSubject(teacherUpdate.getSubject());
					existingTeacher.setJoinDate(teacherUpdate.getJoinDate());
					
					return teacherRepository.save(existingTeacher);
				})
				.orElseThrow(() -> new ResourceNotFound("Teacher", id));
	}
}
