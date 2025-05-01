package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.entity.ExamResult;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;

public interface ExamResultService {

    ExamResult createExamResult(ExamResult examResult);

    ExamResult updateExamResult(Long id, ExamResult examResult);

    List<ExamResult> getStudentResults(Student student);

    List<ExamResult> getTeacherResults(Teacher teacher);

    List<ExamResult> getStudentSubjectResults(Student student, String subject);

    void deleteExamResult(Long id);
} 