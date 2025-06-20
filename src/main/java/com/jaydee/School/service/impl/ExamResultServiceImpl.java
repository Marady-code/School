package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaydee.School.entity.ExamResult;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Subject;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.repository.ExamResultRepository;
import com.jaydee.School.service.ExamResultService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {

	private final ExamResultRepository examResultRepository;

	public ExamResult createExamResult(ExamResult examResult) {
		return examResultRepository.save(examResult);
	}
	public ExamResult updateExamResult(Long id, ExamResult examResult) {
		ExamResult existingResult = examResultRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Exam result not found"));

		existingResult.setScore(examResult.getScore());
		existingResult.setRemarks(examResult.getRemarks());
		existingResult.setExamDate(examResult.getExamDate());
		existingResult.setSubject(examResult.getSubject());

		return examResultRepository.save(existingResult);
	}

	public List<ExamResult> getStudentResults(Student student) {
		return examResultRepository.findByStudent(student);
	}

	public List<ExamResult> getTeacherResults(Teacher teacher) {
		return examResultRepository.findByTeacher(teacher);
	}
	public List<ExamResult> getStudentSubjectResults(Student student, Subject subject) {
		return examResultRepository.findByStudentAndSubject(student, subject);
	}

	public void deleteExamResult(Long id) {
		examResultRepository.deleteById(id);
	}
}