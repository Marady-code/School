package com.jaydee.School.repository;

import com.jaydee.School.entity.ExamResult;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudent(Student student);
    List<ExamResult> findByTeacher(Teacher teacher);
    List<ExamResult> findByStudentAndSubject(Student student, String subject);
} 