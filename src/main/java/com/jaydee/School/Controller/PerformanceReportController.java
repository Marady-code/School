package com.jaydee.School.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.entity.PerformanceReport;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.service.PerformanceReportService;

@RestController
@RequestMapping("/api/performance-reports")
public class PerformanceReportController {

    @Autowired
    private PerformanceReportService performanceReportService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<PerformanceReport> createReport(@RequestBody PerformanceReport report) {
        return ResponseEntity.ok(performanceReportService.createReport(report));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<PerformanceReport> updateReport(
            @PathVariable Long id,
            @RequestBody PerformanceReport report) {
        return ResponseEntity.ok(performanceReportService.updateReport(id, report));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @securityService.isCurrentUser(#studentId)")
    public ResponseEntity<List<PerformanceReport>> getStudentReports(@PathVariable Long studentId) {
        Student student = new Student();
        student.setId(studentId);
        return ResponseEntity.ok(performanceReportService.getStudentReports(student));
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#teacherId)")
    public ResponseEntity<List<PerformanceReport>> getTeacherReports(@PathVariable Long teacherId) {
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        return ResponseEntity.ok(performanceReportService.getTeacherReports(teacher));
    }

    @GetMapping("/student/{studentId}/term/{academicTerm}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @securityService.isCurrentUser(#studentId)")
    public ResponseEntity<List<PerformanceReport>> getStudentTermReports(
            @PathVariable Long studentId,
            @PathVariable String academicTerm) {
        Student student = new Student();
        student.setId(studentId);
        return ResponseEntity.ok(performanceReportService.getStudentTermReports(student, academicTerm));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        performanceReportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }
} 