package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.entity.PerformanceReport;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;

public interface PerformanceReportService {

    PerformanceReport createReport(PerformanceReport report);

    PerformanceReport updateReport(Long id, PerformanceReport report);

    List<PerformanceReport> getStudentReports(Student student);

    List<PerformanceReport> getTeacherReports(Teacher teacher);

    List<PerformanceReport> getStudentTermReports(Student student, String academicTerm);
    
    public void deleteReport(Long id);
} 