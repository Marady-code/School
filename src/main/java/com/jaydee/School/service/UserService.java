package com.jaydee.School.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.jaydee.School.DTO.AdminUserCreationDTO;
import com.jaydee.School.DTO.FirstLoginPasswordChangeDTO;
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
    UserResponse verifyEmail(String token);    UserResponse getUserProfile(Long id);
    UserResponse updateProfilePicture(Long id, MultipartFile file);
    void deleteUser(Long id);
    Page<UserResponse> findUsersWithFilters(UserFilter filter);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);    UserResponse activateUser(Long id);
    UserResponse deactivateUser(Long id);
    UserResponse createUserByAdmin(AdminUserCreationDTO userDTO);
    UserResponse changePasswordFirstLogin(Long userId, FirstLoginPasswordChangeDTO passwordChangeDTO);
    boolean isPasswordChangeRequired(Long userId);
}