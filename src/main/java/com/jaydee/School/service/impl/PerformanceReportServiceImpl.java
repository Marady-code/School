package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaydee.School.entity.PerformanceReport;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.repository.PerformanceReportRepository;
import com.jaydee.School.service.PerformanceReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerformanceReportServiceImpl implements PerformanceReportService {
    
    private final PerformanceReportRepository performanceReportRepository;

    public PerformanceReport createReport(PerformanceReport report) {
        return performanceReportRepository.save(report);
    }

    public PerformanceReport updateReport(Long id, PerformanceReport report) {
        PerformanceReport existingReport = performanceReportRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Performance report not found"));
        
        existingReport.setAverageScore(report.getAverageScore());
        existingReport.setOverallGrade(report.getOverallGrade());
        existingReport.setTeacherComments(report.getTeacherComments());
        existingReport.setStrengths(report.getStrengths());
        existingReport.setAreasForImprovement(report.getAreasForImprovement());
        
        return performanceReportRepository.save(existingReport);
    }

    public List<PerformanceReport> getStudentReports(Student student) {
        return performanceReportRepository.findByStudent(student);
    }

    public List<PerformanceReport> getTeacherReports(Teacher teacher) {
        return performanceReportRepository.findByTeacher(teacher);
    }

    public List<PerformanceReport> getStudentTermReports(Student student, String academicTerm) {
        return performanceReportRepository.findByStudentAndAcademicTerm(student, academicTerm);
    }

    public void deleteReport(Long id) {
        performanceReportRepository.deleteById(id);
    }
} 