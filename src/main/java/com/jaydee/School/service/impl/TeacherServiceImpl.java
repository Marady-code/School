package com.jaydee.School.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.TeacherDTO;
import com.jaydee.School.Exception.CustomAuthenticationException;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.Teacher;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.TeacherMapper;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.TeacherRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

	private final TeacherRepository teacherRepository;
	private final TeacherMapper teacherMapper;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
		// Validate teacher DTO
		validateTeacherDTO(teacherDTO);

		// Check if email already exists
		if (userRepository.existsByEmail(teacherDTO.getEmail())) {
			throw new CustomAuthenticationException("Email already exists");
		}

		// Get teacher role
		Role teacherRole = roleRepository.findByName(Role.RoleName.TEACHER)
				.orElseThrow(() -> new CustomAuthenticationException("Teacher role not found"));

		// 1. Map DTO to Teacher entity
		Teacher teacher = teacherMapper.toEntity(teacherDTO);
		teacher.setFirstName(teacherDTO.getFirstName());
		teacher.setLastName(teacherDTO.getLastName());

		// 2. Create and set up User entity
		User user = new User();
		user.setFirstName(teacherDTO.getFirstName());
		user.setLastName(teacherDTO.getLastName());
		user.setEmail(teacherDTO.getEmail());
		user.setPhoneNumber(teacherDTO.getPhoneNumber());
		user.setUsername(teacherDTO.getEmail());
		String tempPassword = generateTemporaryPassword();
		user.setPassword(passwordEncoder.encode(tempPassword));
		user.setRoles(Set.of(teacherRole));
		user.setRole(Role.RoleName.TEACHER.name());
		user.setIsActive(true);
		user.setIsFirstLogin(true);
		user.setPasswordChangeRequired(true);

		// 3. Link User to Teacher
		teacher.setUser(user);

		// 4. Save Teacher (cascades to User)
		teacher = teacherRepository.save(teacher);

		// 5. Set Teacher in User and save User again (bidirectional)
		user.setTeacher(teacher);
		userRepository.save(user);

		return teacherMapper.toTeacherDTO(teacher);
	}

	private void validateTeacherDTO(TeacherDTO teacherDTO) {
		if (teacherDTO == null) {
			throw new IllegalArgumentException("Teacher data cannot be null");
		}
		if (teacherDTO.getFirstName() == null || teacherDTO.getFirstName().trim().isEmpty()) {
			throw new IllegalArgumentException("First name is required");
		}
		if (teacherDTO.getLastName() == null || teacherDTO.getLastName().trim().isEmpty()) {
			throw new IllegalArgumentException("Last name is required");
		}
		if (teacherDTO.getEmail() == null || teacherDTO.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		}
		if (teacherDTO.getGender() == null) {
			throw new IllegalArgumentException("Gender is required");
		}
		if (teacherDTO.getDob() == null) {
			throw new IllegalArgumentException("Date of birth is required");
		}
		if (teacherDTO.getSpecialization() == null || teacherDTO.getSpecialization().trim().isEmpty()) {
			throw new IllegalArgumentException("Specialization is required");
		}
		if (teacherDTO.getJoinDate() == null) {
			throw new IllegalArgumentException("Join date is required");
		}
	}

	private String generateTemporaryPassword() {
		// Generate a random 8-character password
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder password = new StringBuilder();
		java.util.Random random = new java.util.Random();
		for (int i = 0; i < 8; i++) {
			password.append(chars.charAt(random.nextInt(chars.length())));
		}
		return password.toString();
	}

	@Override
	@Transactional(readOnly = true)
	public TeacherDTO getTeacherById(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		return teacherMapper.toTeacherDTO(teacher);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeacherDTO> getAllTeachers() {
		return teacherRepository.findAll().stream().map(teacherMapper::toTeacherDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
		Teacher existingTeacher = teacherRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Teacher", id));

		// Update the existing teacher entity with values from the DTO
		teacherMapper.updateEntityFromDTO(teacherDTO, existingTeacher);

		// Save the updated teacher
		Teacher updatedTeacher = teacherRepository.save(existingTeacher);
		return teacherMapper.toTeacherDTO(updatedTeacher);
	}

	@Override
	@Transactional
	public void deleteTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		teacherRepository.delete(teacher);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeacherDTO> getTeachersBySubject(String subject) {
		return teacherRepository.findBySubjectsName(subject).stream().map(teacherMapper::toTeacherDTO)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public TeacherDTO activateTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		teacher.getUser().setIsActive(true);
		Teacher activatedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toTeacherDTO(activatedTeacher);
	}

	@Override
	@Transactional
	public TeacherDTO deactivateTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Teacher", id));
		teacher.getUser().setIsActive(false);
		Teacher deactivatedTeacher = teacherRepository.save(teacher);
		return teacherMapper.toTeacherDTO(deactivatedTeacher);
	}
}
