package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.SubjectDTO;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Subject;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.mapper.SubjectMapper;
import com.jaydee.School.repository.SubjectRepository;
import com.jaydee.School.repository.TeacherRepository;
import com.jaydee.School.service.SubjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

	private final SubjectRepository subjectRepository;
	private final TeacherRepository teacherRepository;
	private final SubjectMapper subjectMapper;

	@Override
	@Transactional
	public SubjectDTO createSubject(SubjectDTO subjectDTO) {
		// Validate subject code uniqueness
		if (subjectRepository.existsByCode(subjectDTO.getCode())) {
			throw new IllegalArgumentException("Subject code already exists: " + subjectDTO.getCode());
		}

		Subject subject = subjectMapper.toEntity(subjectDTO);

		// Set teacher if teacherId is provided
		if (subjectDTO.getTeacherId() != null) {
			Teacher teacher = teacherRepository.findById(subjectDTO.getTeacherId())
					.orElseThrow(() -> new ResourceNotFound("Teacher", subjectDTO.getTeacherId()));
			subject.setTeacher(teacher);
		}

		Subject savedSubject = subjectRepository.save(subject);
		return subjectMapper.toDTO(savedSubject);
	}

	@Override
	@Transactional
	public SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) {
		Subject existingSubject = subjectRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Subject", id));

		// Check if new code conflicts with existing subjects
		if (!existingSubject.getCode().equals(subjectDTO.getCode())
				&& subjectRepository.existsByCode(subjectDTO.getCode())) {
			throw new IllegalArgumentException("Subject code already exists: " + subjectDTO.getCode());
		}

		// Update teacher if teacherId is provided
		if (subjectDTO.getTeacherId() != null) {
			Teacher teacher = teacherRepository.findById(subjectDTO.getTeacherId())
					.orElseThrow(() -> new ResourceNotFound("Teacher", subjectDTO.getTeacherId()));
			existingSubject.setTeacher(teacher);
		}

		existingSubject.setName(subjectDTO.getName());
		existingSubject.setCode(subjectDTO.getCode());
		existingSubject.setDescription(subjectDTO.getDescription());
		existingSubject.setCredits(subjectDTO.getCredits());
		existingSubject.setAcademicYear(subjectDTO.getAcademicYear());
		existingSubject.setTerm(subjectDTO.getTerm());

		Subject savedSubject = subjectRepository.save(existingSubject);
		return subjectMapper.toDTO(savedSubject);
	}

	@Override
	public List<SubjectDTO> getSubjectsByTeacher(Long teacherId) {
		Teacher teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new ResourceNotFound("Teacher", teacherId));

		List<Subject> subjects = subjectRepository.findByTeacher(teacher);
		return subjectMapper.toDTOList(subjects);
	}

	@Override
	public List<SubjectDTO> getSubjectsByAcademicYearAndTerm(String academicYear, String term) {
		List<Subject> subjects = subjectRepository.findByAcademicYearAndTerm(academicYear, term);
		return subjectMapper.toDTOList(subjects);
	}

	@Override
	@Transactional
	public void deleteSubject(Long id) {
		if (!subjectRepository.existsById(id)) {
			throw new ResourceNotFound("Subject", id);
		}
		subjectRepository.deleteById(id);
	}

	@Override
	public SubjectDTO getSubjectById(Long id) {
		Subject subject = subjectRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Subject", id));
		return subjectMapper.toDTO(subject);
	}

	@Override
	public List<SubjectDTO> getAllSubjects() {
		List<Subject> subjects = subjectRepository.findAll();
		return subjectMapper.toDTOList(subjects);
	}

	@Override
	public boolean existsByCode(String code) {
		return subjectRepository.existsByCode(code);
	}
}