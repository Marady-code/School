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

import com.jaydee.School.entity.ExamResult;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.service.ExamResultService;

@RestController
@RequestMapping("/exam-results")
public class ExamResultController {

    @Autowired
    private ExamResultService examResultService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ExamResult> createExamResult(@RequestBody ExamResult examResult) {
        return ResponseEntity.ok(examResultService.createExamResult(examResult));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ExamResult> updateExamResult(
            @PathVariable Long id,
            @RequestBody ExamResult examResult) {
        return ResponseEntity.ok(examResultService.updateExamResult(id, examResult));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT') or @securityService.isCurrentUser(#studentId)")
    public ResponseEntity<List<ExamResult>> getStudentResults(@PathVariable Long studentId) {
        Student student = new Student();
        student.setId(studentId);
        return ResponseEntity.ok(examResultService.getStudentResults(student));
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#teacherId)")
    public ResponseEntity<List<ExamResult>> getTeacherResults(@PathVariable Long teacherId) {
        Teacher teacher = new Teacher();
        teacher.setId(teacherId);
        return ResponseEntity.ok(examResultService.getTeacherResults(teacher));
    }

    @GetMapping("/student/{studentId}/subject/{subject}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT') or @securityService.isCurrentUser(#studentId)")
    public ResponseEntity<List<ExamResult>> getStudentSubjectResults(
            @PathVariable Long studentId,
            @PathVariable String subject) {
        Student student = new Student();
        student.setId(studentId);
        return ResponseEntity.ok(examResultService.getStudentSubjectResults(student, subject));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> deleteExamResult(@PathVariable Long id) {
        examResultService.deleteExamResult(id);
        return ResponseEntity.ok().build();
    }
}