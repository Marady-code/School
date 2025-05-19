package com.jaydee.School.service.impl;

import java.util.List;
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

import com.jaydee.School.DTO.AuthenticationRequest;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.Specification.UserFilter;
import com.jaydee.School.Specification.UserSpec;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.UserMapper;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.EmailService;
import com.jaydee.School.service.UserService;

@Service
@Primary
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
			@Lazy AuthenticationManager authenticationManager, EmailService emailService) {
		this.userRepository = userRepository;
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
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
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
			emailService.sendPasswordResetEmail(email, newPassword);
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
		// Send email with new password
		emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
		return userMapper.toDTO(savedUser);
	}

	@Override
	@Transactional
	public UserResponse resetPassword(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));
		String newPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(newPassword));
		User updatedUser = userRepository.save(user);
		// Send email with new password
		emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
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
