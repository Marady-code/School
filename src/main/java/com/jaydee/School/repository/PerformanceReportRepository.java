package com.jaydee.School.repository;

import com.jaydee.School.entity.PerformanceReport;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformanceReportRepository extends JpaRepository<PerformanceReport, Long> {
    List<PerformanceReport> findByStudent(Student student);
    List<PerformanceReport> findByTeacher(Teacher teacher);
    List<PerformanceReport> findByStudentAndAcademicTerm(Student student, String academicTerm);
} 