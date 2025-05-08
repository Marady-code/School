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

import com.jaydee.School.entity.Parent;
import com.jaydee.School.entity.Student;
import com.jaydee.School.service.ParentService;

@RestController
@RequestMapping("/parents")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Parent> createParent(@RequestBody Parent parent) {
        return ResponseEntity.ok(parentService.createParent(parent));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    public ResponseEntity<Parent> updateParent(
            @PathVariable Long id,
            @RequestBody Parent parent) {
        return ResponseEntity.ok(parentService.updateParent(id, parent));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'PARENT') or @securityService.isCurrentUser(#studentId)")
    public ResponseEntity<List<Parent>> getParentsByStudent(@PathVariable Long studentId) {
        Student student = new Student();
        student.setId(studentId);
        return ResponseEntity.ok(parentService.getParentsByStudent(student));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.deleteParent(id);
        return ResponseEntity.ok().build();
    }
}