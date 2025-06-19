package com.jaydee.School.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.jaydee.School.DTO.AdminUserCreationDTO;
import com.jaydee.School.DTO.FirstLoginPasswordChangeDTO;
import com.jaydee.School.DTO.ParentRegistrationDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Specification.UserFilter;
import com.jaydee.School.config.security.AuthenticationRequest;
import com.jaydee.School.entity.User;

public interface UserService extends UserDetailsService {    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse createUser(User user);
    UserResponse updateUser(Long id, User user);
    UserResponse updatePassword(Long id, String oldPassword, String newPassword);
    UserResponse login(AuthenticationRequest loginDTO);
    UserResponse register(User user);    UserResponse resetPassword(Long id);
    void requestPasswordReset(String email);
    UserResponse resetPassword(String email);
//    UserResponse verifyEmail(String token);
//    void sendVerificationEmail(String email);
    UserResponse getUserProfile(Long id);
    void deleteUser(Long id);
    Page<UserResponse> findUsersWithFilters(UserFilter filter);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);    UserResponse activateUser(Long id);
    UserResponse deactivateUser(Long id);
    UserResponse createUserByAdmin(AdminUserCreationDTO userDTO);
    UserResponse changePasswordFirstLogin(Long userId, FirstLoginPasswordChangeDTO passwordChangeDTO);
    boolean isPasswordChangeRequired(Long userId);
    UserResponse registerParent(ParentRegistrationDTO registrationDTO);
    
//    UserResponse createStudentOrTeacher(String email, String password, String firstName, String lastName, 
//            String phoneNumber, Role.RoleName roleName, Map<String, Object> additionalData);
}