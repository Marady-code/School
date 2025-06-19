package com.jaydee.School.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jaydee.School.DTO.AdminResponse;
import com.jaydee.School.entity.AdminCreateRequest;
import com.jaydee.School.entity.AdminUpdateRequest;

public interface AdminService {
    AdminResponse createAdmin(AdminCreateRequest request);
    AdminResponse updateAdmin(Long id, AdminUpdateRequest request);
    void deleteAdmin(Long id);
    AdminResponse getAdminById(Long id);
    Page<AdminResponse> getAllAdmins(Pageable pageable);
    List<AdminResponse> getAllAdmins();
    AdminResponse activateAdmin(Long id);
    AdminResponse deactivateAdmin(Long id);
    void resetAdminPassword(Long id);
}