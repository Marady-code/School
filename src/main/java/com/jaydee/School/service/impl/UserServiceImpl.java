package com.jaydee.School.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jaydee.School.DTO.AdminUserCreationDTO;
import com.jaydee.School.DTO.FirstLoginPasswordChangeDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.Specification.UserFilter;
import com.jaydee.School.Specification.UserSpec;
import com.jaydee.School.config.security.AuthenticationRequest;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.UserMapper;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.EmailService;
import com.jaydee.School.service.UserService;

@Service
@Primary
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper,
			PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager,
			EmailService emailService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.emailService = emailService;
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		return userMapper.mapToUserResponse(user);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		return userMapper.mapToUserResponse(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(userMapper::mapToUserResponse).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserResponse updatePassword(Long userId, String oldPassword, String newPassword) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User", userId));

		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new IllegalArgumentException("Invalid old password");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		return userMapper.mapToUserResponse(userRepository.save(user));
	}

	@Override
	public UserResponse login(AuthenticationRequest loginRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		return userMapper.mapToUserResponse(userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
				() -> new ResourceNotFound(String.format("User not found with email: %s", loginRequest.getEmail()))));
	}

	@Override
	@Transactional
	public UserResponse createUser(User user) {
		User savedUser = userRepository.save(user);
		return userMapper.mapToUserResponse(savedUser);
	}

	@Override
	@Transactional
	public UserResponse updateUser(Long id, User user) {
		User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

		userMapper.updateEntityFromDTO(userMapper.mapToUserResponse(user), existingUser);
		User updatedUser = userRepository.save(existingUser);
		return userMapper.mapToUserResponse(updatedUser);
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		userRepository.delete(user);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserResponse> findUsersWithFilters(UserFilter filter) {
		Specification<User> spec = UserSpec.withFilters(filter);

		PageRequest pageable = PageRequest.of(filter.getPage(), filter.getSize(), filter.getDirection(),
				filter.getSortBy());

		return userRepository.findAll(spec, pageable).map(userMapper::mapToUserResponse);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
	}

	@Override
	public UserResponse register(User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userMapper.toDTO(userRepository.save(user));
	}

	@Transactional
	public void requestPasswordReset(String email) {
		userRepository.findByEmail(email).ifPresent(user -> {
			String newPassword = generateRandomPassword();
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);

			// Try to send email, but don't fail if it doesn't work
			try {
				emailService.sendPasswordResetEmail(email, newPassword);
			} catch (Exception e) {
				// Log the error but continue
				System.err.println("Failed to send password reset email: " + e.getMessage());
			}
		});
		// We don't throw an exception if email is not found for security reasons
	}

	@Override
	@Transactional
	public UserResponse resetPassword(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		String newPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(newPassword));
		User savedUser = userRepository.save(user);

		// Try to send email, but don't fail if it doesn't work
		try {
			emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
		} catch (Exception e) {
			// Log the error but continue
			System.err.println("Failed to send password reset email: " + e.getMessage());
		}

		return userMapper.toDTO(savedUser);
	}

	@Override
	@Transactional
	public UserResponse resetPassword(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));
		String newPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(newPassword));
		User updatedUser = userRepository.save(user);

		// Try to send email, but don't fail if it doesn't work
		try {
			emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
		} catch (Exception e) {
			// Log the error but continue
			System.err.println("Failed to send password reset email: " + e.getMessage());
		}

		return userMapper.mapToUserResponse(updatedUser);
	}

	@Override
	public UserResponse verifyEmail(String token) {
		// TODO: Implement email verification logic
		return null;
	}

	@Override
	public UserResponse getUserProfile(Long id) {
		return userMapper.toDTO(userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
	}

	@Override
	public UserResponse updateProfilePicture(Long id, MultipartFile file) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		// TODO: Implement profile picture upload logic
		return userMapper.toDTO(user);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	@Transactional
	public UserResponse activateUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));
		user.setIsActive(true);
		User activatedUser = userRepository.save(user);
		return userMapper.mapToUserResponse(activatedUser);
	}

	@Override
	@Transactional
	public UserResponse deactivateUser(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));
		user.setIsActive(false);
		User deactivatedUser = userRepository.save(user);
		return userMapper.mapToUserResponse(deactivatedUser);
	}

	@Override
	@Transactional
	public UserResponse createUserByAdmin(AdminUserCreationDTO userDTO) {
		// Check if username or email already exists
		if (userRepository.existsByUsername(userDTO.getUsername())) {
			throw new RuntimeException("Username already exists");
		}
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new RuntimeException("Email already exists");
		}

		// Create new user
		User user = new User();
		user.setUsername(userDTO.getUsername());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		user.setPhoneNumber(userDTO.getPhoneNumber());
		user.setIsActive(true);

		// Generate a temporary password
		String temporaryPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(temporaryPassword));

		// Set password change required flag
		user.setPasswordChangeRequired(true);

		// Handle role assignment
		Role.RoleName roleName;
		try {
			roleName = Role.RoleName.valueOf(userDTO.getRole());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Invalid role: " + userDTO.getRole());
		}

		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
		user.setRoles(Set.of(role));
		user.setRole(roleName.name());

		User savedUser = userRepository.save(user);

		// Send email with credentials if requested
		if (userDTO.isSendCredentialsEmail()) {
			try {
				emailService.sendWelcomeEmail(user.getEmail(),
						"Welcome to School Management System. Your temporary password is: " + temporaryPassword
								+ ". Please change your password when you first login.");
			} catch (Exception e) {
				// Log but don't fail the operation
				System.err.println("Failed to send welcome email: " + e.getMessage());
			}
		}

		return userMapper.mapToUserResponse(savedUser);
	}

	@Override
	@Transactional
	public UserResponse changePasswordFirstLogin(Long userId, FirstLoginPasswordChangeDTO passwordChangeDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", userId));

		// Verify temporary password
		if (!passwordEncoder.matches(passwordChangeDTO.getTemporaryPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid temporary password");
		}

		// Check if passwords match
		if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
			throw new IllegalArgumentException("New password and confirmation do not match");
		}

		// Update password
		user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
		user.setPasswordChangeRequired(false);
		user.setLastPasswordChangeDate(LocalDateTime.now());

		User updatedUser = userRepository.save(user);
		return userMapper.mapToUserResponse(updatedUser);
	}

	@Override
	public boolean isPasswordChangeRequired(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFound("User", userId));
		return user.getPasswordChangeRequired() != null && user.getPasswordChangeRequired();
	}

	private String generateRandomPassword() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
		StringBuilder sb = new StringBuilder();
		int length = 12;
		for (int i = 0; i < length; i++) {
			int index = (int) (chars.length() * Math.random());
			sb.append(chars.charAt(index));
		}
		return sb.toString();
	}
}
