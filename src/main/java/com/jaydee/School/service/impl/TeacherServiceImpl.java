package com.jaydee.School.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.mapper.TeacherMapper;
import com.jaydee.School.repository.TeacherRepository;
import com.jaydee.School.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

	private final TeacherRepository teacherRepository;
	private final TeacherMapper teacherMapper;

	@Override
	@Transactional
	public TeacherDTO createTeacher(Teacher teacher) {
		Teacher savedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toTeacherDTO(savedTeacher);
	}
	@Override
	@Transactional(readOnly = true)
	public TeacherDTO getTeacherById(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		return teacherMapper.toTeacherDTO(teacher);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeacherDTO> getAllTeachers() {
		return teacherRepository.findAll().stream().map(teacherMapper::toTeacherDTO).collect(Collectors.toList());
	}
	@Override
	@Transactional
	public TeacherDTO updateTeacher(Long id, Teacher teacherUpdate) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));

		if (teacherUpdate.getFirstName() != null && !teacherUpdate.getFirstName().equals(teacher.getFirstName())) {
			teacher.setFirstName(teacherUpdate.getFirstName());
		}
		if (teacherUpdate.getLastName() != null && !teacherUpdate.getLastName().equals(teacher.getLastName())) {
			teacher.setLastName(teacherUpdate.getLastName());
		}
		if (teacherUpdate.getEmail() != null && !teacherUpdate.getEmail().equals(teacher.getEmail())) {
			teacher.setEmail(teacherUpdate.getEmail());
		}
		if (teacherUpdate.getPhoneNumber() != null
				&& !teacherUpdate.getPhoneNumber().equals(teacher.getPhoneNumber())) {
			teacher.setPhoneNumber(teacherUpdate.getPhoneNumber());
		}
		if (teacherUpdate.getQualification() != null
				&& !teacherUpdate.getQualification().equals(teacher.getQualification())) {
			teacher.setQualification(teacherUpdate.getQualification());
		}
		if (teacherUpdate.getSpecialization() != null
				&& !teacherUpdate.getSpecialization().equals(teacher.getSpecialization())) {
			teacher.setSpecialization(teacherUpdate.getSpecialization());
		}
		if (teacherUpdate.getExperienceYears() != null
				&& !teacherUpdate.getExperienceYears().equals(teacher.getExperienceYears())) {
			teacher.setExperienceYears(teacherUpdate.getExperienceYears());
		}

		Teacher updatedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toTeacherDTO(updatedTeacher);
	}
	@Override
	@Transactional
	public void deleteTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		teacherRepository.delete(teacher);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeacherDTO> getTeachersBySubject(String subject) {
		return teacherRepository.findBySubjectsName(subject).stream().map(teacherMapper::toTeacherDTO)
				.collect(Collectors.toList());
	}
	@Override
	@Transactional
	public TeacherDTO activateTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		teacher.setIsActive(true);
		Teacher activatedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toTeacherDTO(activatedTeacher);
	}

	@Override
	@Transactional
	public TeacherDTO deactivateTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		teacher.setIsActive(false);
		Teacher deactivatedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toTeacherDTO(deactivatedTeacher);
	}
}
