package com.jaydee.School.service;

import com.jaydee.School.entity.ExamResult;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.repository.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamResultService {
    
    @Autowired
    private ExamResultRepository examResultRepository;

    public ExamResult createExamResult(ExamResult examResult) {
        return examResultRepository.save(examResult);
    }

    public ExamResult updateExamResult(Long id, ExamResult examResult) {
        ExamResult existingResult = examResultRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Exam result not found"));
        
        existingResult.setScore(examResult.getScore());
        existingResult.setRemarks(examResult.getRemarks());
        existingResult.setExamDate(examResult.getExamDate());
        
        return examResultRepository.save(existingResult);
    }

    public List<ExamResult> getStudentResults(Student student) {
        return examResultRepository.findByStudent(student);
    }

    public List<ExamResult> getTeacherResults(Teacher teacher) {
        return examResultRepository.findByTeacher(teacher);
    }

    public List<ExamResult> getStudentSubjectResults(Student student, String subject) {
        return examResultRepository.findByStudentAndSubject(student, subject);
    }

    public void deleteExamResult(Long id) {
        examResultRepository.deleteById(id);
    }
} 