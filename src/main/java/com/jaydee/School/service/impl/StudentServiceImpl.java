package com.jaydee.School.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.StudentDTO;
import com.jaydee.School.DTO.StudentRegistrationDTO;
import com.jaydee.School.Exception.CustomAuthenticationException;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.StudentMapper;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public StudentDTO registerStudent(StudentRegistrationDTO registrationDTO) {
		// Check if email already exists
		if (userRepository.existsByEmail(registrationDTO.getEmail())) {
			throw new CustomAuthenticationException("Email already exists");
		}

		// Get student role
		Role studentRole = roleRepository.findByName(Role.RoleName.STUDENT)
				.orElseThrow(() -> new CustomAuthenticationException("Student role not found"));

		// Create user account
		User user = new User();
		user.setFirstName(registrationDTO.getFirstName());
		user.setLastName(registrationDTO.getLastName());
		user.setEmail(registrationDTO.getEmail());
		user.setUsername(registrationDTO.getEmail());
		// Generate a temporary password - student will be required to change it on first login
		String tempPassword = generateTemporaryPassword();
		user.setPassword(passwordEncoder.encode(tempPassword));
		user.setPhoneNumber(registrationDTO.getPhoneNumber());
		user.setRoles(Set.of(studentRole));
		user.setRole(Role.RoleName.STUDENT.name());
		user.setIsActive(true);
		user.setIsFirstLogin(true);
		user.setPasswordChangeRequired(true);

		// Create student profile
		Student student = new Student();
		student.setUser(user);
		student.setGender(registrationDTO.getGender());
		student.setDob(registrationDTO.getDob());
		student.setAddress(registrationDTO.getAddress());
		student.setEmergencyContact(registrationDTO.getEmergencyContact());
		student.setEmergencyPhone(registrationDTO.getEmergencyPhone());
		student.setIsActive(true);

		// Save both entities
		user = userRepository.save(user);
		student = studentRepository.save(student);

		return studentMapper.toStudentDTO(student);
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
	public Student createStudent(Student student) {
		return studentRepository.save(student);
	}

	@Override
	public StudentDTO getStudentById(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));
		return studentMapper.toStudentDTO(student);
	}

	@Override
	public List<StudentDTO> getAllStudents() {
		return studentRepository.findAll().stream().map(studentMapper::toStudentDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
		Student existingStudent = studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("Student", id));

		// Update the existing student entity with values from the DTO
		studentMapper.updateEntityFromDTO(studentDTO, existingStudent);

		// Save the updated student
		Student updatedStudent = studentRepository.save(existingStudent);
		return studentMapper.toStudentDTO(updatedStudent);
	}

	@Override
	public void deleteStudent(Long id) {
		if (!studentRepository.existsById(id)) {
			throw new ResourceNotFound("Student", id);
		}
		studentRepository.deleteById(id);
	}

	@Override
	public List<StudentDTO> getStudentsByClass(String className) {
		return studentRepository.findByClassEntity_ClassName(className).stream().map(studentMapper::toStudentDTO)
				.collect(Collectors.toList());
	}

	@Override
	public StudentDTO activateStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));
		student.setIsActive(true);
		Student updatedStudent = studentRepository.save(student);
		return studentMapper.toStudentDTO(updatedStudent);
	}

	@Override
	public StudentDTO deactivateStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Student", id));
		student.setIsActive(false);
		Student updatedStudent = studentRepository.save(student);
		return studentMapper.toStudentDTO(updatedStudent);
	}

	private void validateStudent(Student student) {
		if (student == null) {
			throw new IllegalArgumentException("Student cannot be null");
		}
		if (student.getUser() == null) {
			throw new IllegalArgumentException("Student must have an associated user");
		}
		if (student.getUser().getFirstName() == null || student.getUser().getFirstName().trim().isEmpty()) {
			throw new IllegalArgumentException("First name cannot be null or empty");
		}
		if (student.getUser().getLastName() == null || student.getUser().getLastName().trim().isEmpty()) {
			throw new IllegalArgumentException("Last name cannot be null or empty");
		}
		if (student.getGender() == null) {
			throw new IllegalArgumentException("Gender cannot be null");
		}
		if (student.getDob() == null) {
			throw new IllegalArgumentException("Date of birth cannot be null");
		}
		if (student.getDob().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Date of birth cannot be in the future");
		}
	}
}
