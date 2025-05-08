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

import com.jaydee.School.DTO.AdminCreateRequest;
import com.jaydee.School.DTO.AdminUpdateRequest;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.service.AdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<UserResponse> createAdmin(@Valid @RequestBody AdminCreateRequest request) {
        return ResponseEntity.ok(adminService.createAdmin(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateRequest request
    ) {
        return ResponseEntity.ok(adminService.updateAdmin(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activateAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateAdmin(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deactivateAdmin(id));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetAdminPassword(@PathVariable Long id) {
        adminService.resetAdminPassword(id);
        return ResponseEntity.noContent().build();
    }
} 