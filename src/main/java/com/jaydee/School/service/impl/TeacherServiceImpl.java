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
public class TeacherServiceImpl implements TeacherService {

	private final TeacherRepository teacherRepository;

	@Override
	@Transactional
	public Teacher create(Teacher teacher) {
		validateTeacher(teacher);
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
	@Transactional
	public void deleteTeacherById(Long id) {
		if (!teacherRepository.existsById(id)) {
			throw new ResourceNotFound("Teacher", id);
		}
		teacherRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Teacher updateTeacher(Long id, Teacher teacherUpdate) {
		return teacherRepository.findById(id)
				.map(existingTeacher -> {
					if (teacherUpdate.getName() != null && !teacherUpdate.getName().isBlank()) {
						existingTeacher.setName(teacherUpdate.getName());
					}
					if (teacherUpdate.getDob() != null) {
						existingTeacher.setDob(teacherUpdate.getDob());
					}
					if (teacherUpdate.getPhoneNumber() != null) {
						existingTeacher.setPhoneNumber(teacherUpdate.getPhoneNumber());
					}
					if (teacherUpdate.getSubject() != null && !teacherUpdate.getSubject().isBlank()) {
						existingTeacher.setSubject(teacherUpdate.getSubject());
					}
					if (teacherUpdate.getJoinDate() != null) {
						existingTeacher.setJoinDate(teacherUpdate.getJoinDate());
					}
					return teacherRepository.save(existingTeacher);
				})
				.orElseThrow(() -> new ResourceNotFound("Teacher", id));
	}

	private void validateTeacher(Teacher teacher) {
		if (teacher.getName() == null || teacher.getName().isBlank()) {
			throw new IllegalArgumentException("Teacher name cannot be null or empty");
		}
		if (teacher.getSubject() == null || teacher.getSubject().isBlank()) {
			throw new IllegalArgumentException("Teacher subject cannot be null or empty");
		}
		if (teacher.getPhoneNumber() != null && !teacher.getPhoneNumber().matches("\\d{10}")) {
			throw new IllegalArgumentException("Phone number must be 10 digits");
		}
		if (teacher.getJoinDate() == null) {
			throw new IllegalArgumentException("Join date cannot be null");
		}
	}
}
