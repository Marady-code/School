package com.jaydee.School.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.AdminCreateRequest;
import com.jaydee.School.DTO.AdminUpdateRequest;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.config.security.Role;
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

	@Override
	@Transactional
	public UserResponse createAdmin(AdminCreateRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
				.orElseThrow(() -> new RuntimeException("Admin role not found"));

		User admin = new User();
		admin.setFirstName(request.getFirstName());
		admin.setLastName(request.getLastName());
		admin.setEmail(request.getEmail());
		admin.setPassword(passwordEncoder.encode(request.getPassword()));
		Set<Role> roles = new HashSet<>();
		roles.add(adminRole);
		admin.setRoles(roles);
		admin.setIsActive(true);

		User savedAdmin = userRepository.save(admin);
		emailService.sendWelcomeEmail(savedAdmin.getEmail(), savedAdmin.getFirstName());

		return userMapper.mapToUserResponse(savedAdmin);
	}

	@Override
	@Transactional
	public UserResponse updateAdmin(Long id, AdminUpdateRequest request) {
		User admin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));

		admin.setFirstName(request.getFirstName());
		admin.setLastName(request.getLastName());
		if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
			admin.setPassword(passwordEncoder.encode(request.getNewPassword()));
		}

		User updatedAdmin = userRepository.save(admin);
		return userMapper.mapToUserResponse(updatedAdmin);
	}

	@Override
	@Transactional
	public void deleteAdmin(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
		userRepository.delete(admin);
	}

	@Override
	public UserResponse getAdminById(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
		return userMapper.mapToUserResponse(admin);
	}

	@Override
	public Page<UserResponse> getAllAdmins(Pageable pageable) {
		Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
				.orElseThrow(() -> new RuntimeException("Admin role not found"));
		return userRepository.findByRolesContaining(adminRole, pageable).map(userMapper::mapToUserResponse);
	}

	@Override
	public List<UserResponse> getAllAdmins() {
		Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
				.orElseThrow(() -> new RuntimeException("Admin role not found"));
		return userRepository.findByRolesContaining(adminRole).stream().map(userMapper::mapToUserResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserResponse activateAdmin(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));

		if (!admin.hasRole(Role.RoleName.ADMIN)) {
			throw new IllegalArgumentException("User is not an admin");
		}

		admin.setIsActive(true);
		User activatedAdmin = userRepository.save(admin);
		return userMapper.mapToUserResponse(activatedAdmin);
	}

	@Override
	@Transactional
	public UserResponse deactivateAdmin(Long id) {
		User admin = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));

		if (!admin.hasRole(Role.RoleName.ADMIN)) {
			throw new IllegalArgumentException("User is not an admin");
		}

		admin.setIsActive(false);
		User deactivatedAdmin = userRepository.save(admin);
		return userMapper.mapToUserResponse(deactivatedAdmin);
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

		emailService.sendPasswordResetEmail(admin.getEmail(), newPassword);
	}

	private String generateRandomPassword() {
		// Implement a secure random password generation logic
		return "TemporaryPassword123!";
	}
}