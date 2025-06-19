package com.jaydee.School.service.impl;

import java.security.SecureRandom;
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
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	//private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

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
	}

	private String generateRandomPassword() {
		String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerChars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
		String allChars = upperChars + lowerChars + numbers + specialChars;
		
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder();
		
		// Ensure at least one of each character type
		password.append(upperChars.charAt(random.nextInt(upperChars.length())));
		password.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
		password.append(numbers.charAt(random.nextInt(numbers.length())));
		password.append(specialChars.charAt(random.nextInt(specialChars.length())));
		
		// Add remaining characters
		for (int i = 4; i < 12; i++) {
			password.append(allChars.charAt(random.nextInt(allChars.length())));
		}
		
		// Shuffle the password
		char[] passwordArray = password.toString().toCharArray();
		for (int i = passwordArray.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			char temp = passwordArray[i];
			passwordArray[i] = passwordArray[j];
			passwordArray[j] = temp;
		}
		
		return new String(passwordArray);
	}
}