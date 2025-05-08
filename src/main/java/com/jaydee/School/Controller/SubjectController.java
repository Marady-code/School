package com.jaydee.School.Controller;

import java.util.List;

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

import com.jaydee.School.DTO.SubjectDTO;
import com.jaydee.School.service.SubjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        return ResponseEntity.ok(subjectService.createSubject(subjectDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectDTO subjectDTO) {
        return ResponseEntity.ok(subjectService.updateSubject(id, subjectDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(subjectService.getSubjectsByTeacher(teacherId));
    }
}