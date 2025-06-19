package com.jaydee.School.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.ParentRegistrationDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Exception.CustomAuthenticationException;
import com.jaydee.School.entity.Parent;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.Student;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.UserMapper;
import com.jaydee.School.repository.ParentRepository;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.StudentRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.ParentRegistrationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentRegistrationServiceImpl implements ParentRegistrationService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse registerParent(ParentRegistrationDTO registrationDTO) {
        // Validate passwords match
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new CustomAuthenticationException("Passwords do not match");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new CustomAuthenticationException("Email already exists");
        }

        // Find student by phone number
        Student student = studentRepository.findByPhoneNumber(registrationDTO.getStudentPhoneNumber())
                .orElseThrow(() -> new CustomAuthenticationException("No student found with the provided phone number"));

        // Get parent role
        Role parentRole = roleRepository.findByName(Role.RoleName.PARENT)
                .orElseThrow(() -> new CustomAuthenticationException("Parent role not found"));

        // Create user account
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

    @Override
    public boolean verifyStudentPhoneNumber(String studentPhoneNumber) {
        return studentRepository.findByPhoneNumber(studentPhoneNumber).isPresent();
    }
} 