package com.jaydee.School.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.AdminResponse;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.AdminCreateRequest;
import com.jaydee.School.entity.AdminUpdateRequest;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.UserMapper;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.AdminService;
import com.jaydee.School.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	/**
	 * Helper method to convert User to AdminResponse
	 */
	private AdminResponse toAdminResponse(User user) {
		return AdminResponse.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail())
				.isActive(user.getIsActive())
				.roles(user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()))
				.createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
	}

	@Override
	@Transactional
	public AdminResponse createAdmin(AdminCreateRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
				.orElseThrow(() -> new RuntimeException("Admin role not found"));

		User admin = new User();
		admin.setUsername(request.getUsername());
		admin.setEmail(request.getEmail());
		admin.setPassword(passwordEncoder.encode(request.getPassword()));
		admin.getRoles().add(adminRole); // Add to the roles collection
		admin.setRole("ADMIN"); // Also set the role string
		admin.setIsActive(true);

		// Save the admin user first
		User savedAdmin = userRepository.save(admin);

		// Try to send welcome email, but don't fail if it doesn't work
		try {
			emailService.sendWelcomeEmail(savedAdmin.getEmail(), savedAdmin.getUsername());
		} catch (Exception e) {
			// Log the error but don't prevent admin creation
			System.err.println("Failed to send welcome email: " + e.getMessage());
		}

		return toAdminResponse(savedAdmin);
	}

	@Override
	@Transactional
	public AdminResponse updateAdmin(Long id, AdminUpdateRequest request) {
		User admin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));

		admin.setUsername(request.getUserName());
		if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
			admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
		}

		User updatedAdmin = userRepository.save(admin);
		return toAdminResponse(updatedAdmin);
	}

	@Override
	@Transactional
	public void deleteAdmin(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
		userRepository.delete(admin);
	}

	@Override
	public AdminResponse getAdminById(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
		return toAdminResponse(admin);
	}

	@Override
	public Page<AdminResponse> getAllAdmins(Pageable pageable) {
		Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
				.orElseThrow(() -> new RuntimeException("Admin role not found"));
		return userRepository.findByRolesContaining(adminRole, pageable).map(this::toAdminResponse);
	}

	@Override
	public List<AdminResponse> getAllAdmins() {
		Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
				.orElseThrow(() -> new RuntimeException("Admin role not found"));
		return userRepository.findByRolesContaining(adminRole).stream().map(this::toAdminResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AdminResponse activateAdmin(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));

		if (!admin.hasRole(Role.RoleName.ADMIN)) {
			throw new IllegalArgumentException("User is not an admin");
		}

		admin.setIsActive(true);
		User activatedAdmin = userRepository.save(admin);
		return toAdminResponse(activatedAdmin);
	}

	@Override
	@Transactional
	public AdminResponse deactivateAdmin(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));

		if (!admin.hasRole(Role.RoleName.ADMIN)) {
			throw new IllegalArgumentException("User is not an admin");
		}

		admin.setIsActive(false);
		User deactivatedAdmin = userRepository.save(admin);
		return toAdminResponse(deactivatedAdmin);
	}

	@Override
	@Transactional
	public void resetAdminPassword(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));

		if (!admin.hasRole(Role.RoleName.ADMIN)) {
			throw new IllegalArgumentException("User is not an admin");
		}

		String newPassword = generateRandomPassword();
		admin.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(admin);

		// Try to send password reset email, but don't fail if it doesn't work
		try {
			emailService.sendPasswordResetEmail(admin.getEmail(), newPassword);
		} catch (Exception e) {
			// Log the error but don't prevent password reset
			System.err.println("Failed to send password reset email: " + e.getMessage());
		}
	}

	private String generateRandomPassword() {
		// Implement a secure random password generation logic
		return "TemporaryPassword123!";
	}
}