package com.jaydee.School.service;

import com.jaydee.School.DTO.AdminCreateRequest;
import com.jaydee.School.DTO.AdminUpdateRequest;
import com.jaydee.School.DTO.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    UserResponse createAdmin(AdminCreateRequest request);
    UserResponse updateAdmin(Long id, AdminUpdateRequest request);
    void deleteAdmin(Long id);
    UserResponse getAdminById(Long id);
    Page<UserResponse> getAllAdmins(Pageable pageable);
    List<UserResponse> getAllAdmins();
    UserResponse activateAdmin(Long id);
    UserResponse deactivateAdmin(Long id);
    void resetAdminPassword(Long id);
} 