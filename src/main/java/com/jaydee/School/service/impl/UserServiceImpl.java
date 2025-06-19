package com.jaydee.School.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.jaydee.School.DTO.AdminUserCreationDTO;
import com.jaydee.School.DTO.FirstLoginPasswordChangeDTO;
import com.jaydee.School.DTO.ParentRegistrationDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Exception.CustomAuthenticationException;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.Specification.UserFilter;
import com.jaydee.School.Specification.UserSpec;
import com.jaydee.School.config.security.AuthenticationRequest;
import com.jaydee.School.entity.Parent;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.UserMapper;
import com.jaydee.School.repository.ParentRepository;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Primary
public class UserServiceImpl implements UserService {

	private static final String EMAIL_VERIFICATION_SECRET = "email-verification-secret-key";
	private static final long EMAIL_VERIFICATION_EXPIRATION = 24 * 60 * 60 * 1000; // 24 hours

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final StudentRepository studentRepository;
	private final ParentRepository parentRepository;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper,
			PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager,
			StudentRepository studentRepository, ParentRepository parentRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.studentRepository = studentRepository;
		this.parentRepository = parentRepository;
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
		User existingUser = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFound("User", id));

		userMapper.updateEntityFromDTO(userMapper.mapToUserResponse(user), existingUser);
		User updatedUser = userRepository.save(existingUser);
		return userMapper.mapToUserResponse(updatedUser);
	}

	@Override
	@Transactional
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFound("User", id));
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
		});
		// We don't throw an exception if email is not found for security reasons
	}

	@Override
	@Transactional
	public UserResponse resetPassword(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new ResourceNotFound("User with email", email));
		String newPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(newPassword));
		User savedUser = userRepository.save(user);

		return userMapper.mapToUserResponse(savedUser);
	}

	@Override
	@Transactional
	public UserResponse resetPassword(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFound("User", id));
		String newPassword = generateRandomPassword();
		user.setPassword(passwordEncoder.encode(newPassword));
		User updatedUser = userRepository.save(user);

		return userMapper.mapToUserResponse(updatedUser);
	}

	private String generateEmailVerificationToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", email);
		claims.put("type", "email-verification");
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EMAIL_VERIFICATION_EXPIRATION))
				.signWith(SignatureAlgorithm.HS512, EMAIL_VERIFICATION_SECRET)
				.compact();
	}

//	@Override
//	public UserResponse verifyEmail(String token) {
//		try {
//			Claims claims = Jwts.parser()
//					.setSigningKey(EMAIL_VERIFICATION_SECRET)
//					.parseClaimsJws(token)
//					.getBody();
//
//			String email = claims.get("email", String.class);
//			String type = claims.get("type", String.class);
//
//			if (!"email-verification".equals(type)) {
//				throw new IllegalArgumentException("Invalid token type");
//			}
//
//			User user = userRepository.findByEmail(email)
//					.orElseThrow(() -> new ResourceNotFound("User with email", email));
//
//			User verifiedUser = userRepository.save(user);
//
//			return userMapper.mapToUserResponse(verifiedUser);
//		} catch (ExpiredJwtException e) {
//			throw new IllegalArgumentException("Email verification token has expired");
//		} catch (JwtException e) {
//			throw new IllegalArgumentException("Invalid email verification token");
//		}
//	}


	@Override
	public UserResponse getUserProfile(Long id) {
		return userMapper.mapToUserResponse(
			userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("User", id))
		);
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

		// Set password change required flag for Student/Teacher
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

		// Only require password change for Student or Teacher
		if (roleName == Role.RoleName.STUDENT || roleName == Role.RoleName.TEACHER) {
			user.setPasswordChangeRequired(true);
			user.setIsFirstLogin(true);
		} else {
			user.setPasswordChangeRequired(false);
			user.setIsFirstLogin(false);
		}

		User savedUser = userRepository.save(user);

		return userMapper.mapToUserResponse(savedUser);
	}

	@Override
	@Transactional
	public UserResponse changePasswordFirstLogin(Long userId, FirstLoginPasswordChangeDTO passwordChangeDTO) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User", userId));

		// Only allow if password change is required
		if (user.getPasswordChangeRequired() == null || !user.getPasswordChangeRequired()) {
			throw new IllegalStateException("Password change is not required for this user");
		}

		// Verify temporary password
		if (!passwordEncoder.matches(passwordChangeDTO.getTemporaryPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid temporary password");
		}

		// Check if passwords match
		if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
			throw new IllegalArgumentException("New password and confirmation do not match");
		}

		// Update password and flags
		user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
		user.setPasswordChangeRequired(false);
		user.setIsFirstLogin(false);
		user.setLastPasswordChangeDate(LocalDateTime.now());

		User updatedUser = userRepository.save(user);
		return userMapper.mapToUserResponse(updatedUser);
	}

	@Override
	public boolean isPasswordChangeRequired(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User", userId));
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

	@Transactional
	public UserResponse registerParent(ParentRegistrationDTO registrationDTO) {
		// Check if email already exists
		if (userRepository.existsByEmail(registrationDTO.getEmail())) {
			throw new CustomAuthenticationException("Email already exists");
		}

		// Find student by phone number
		Student student = studentRepository.findByPhoneNumber(registrationDTO.getStudentPhoneNumber()).orElseThrow(
				() -> new CustomAuthenticationException("No student found with the provided phone number"));

		// Get parent role
		Role parentRole = roleRepository.findByName(Role.RoleName.PARENT)
				.orElseThrow(() -> new CustomAuthenticationException("Parent role not found"));

		// Create user
		User user = new User();
		user.setFirstName(registrationDTO.getFirstName());
		user.setLastName(registrationDTO.getLastName());
		user.setEmail(registrationDTO.getEmail());
		user.setUsername(registrationDTO.getEmail());
		user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
		user.setPhoneNumber(registrationDTO.getPhoneNumber());
		user.setRoles(Set.of(parentRole));
		user.setRole(Role.RoleName.PARENT.name());
		user.setIsActive(true);
		user.setIsFirstLogin(false); // Parents don't need to change password on first login

		// Create parent entity
		Parent parent = new Parent();
		parent.setUser(user);
		parent.setRelationship(registrationDTO.getRelationship());
		parent.setOccupation(registrationDTO.getOccupation());
		parent.setAddress(registrationDTO.getAddress());
		parent.setEmergencyContact(registrationDTO.getEmergencyContact());
		parent.setStudents(List.of(student));

		// Save both entities
		user = userRepository.save(user);
		parentRepository.save(parent);

		return userMapper.mapToUserResponse(user);
	}
}