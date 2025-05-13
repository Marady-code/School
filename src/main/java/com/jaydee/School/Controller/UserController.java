package com.jaydee.School.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema; // Not used
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.jaydee.School.DTO.PasswordChangeDTO;
import com.jaydee.School.DTO.UserRegistrationDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Specification.UserFilter;
import com.jaydee.School.entity.User;
import com.jaydee.School.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.jaydee.School.Exception.InvalidRequestException;
import com.jaydee.School.service.UserSecurityService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing user accounts")
@SecurityRequirement(name = "JWT")
public class UserController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;
      @Operation(
        summary = "Get all users", 
        description = "Retrieves a list of all users in the system. Accessible only by SUPER_ADMIN and ADMIN roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user list"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
      @Operation(
        summary = "Search users with filters", 
        description = "Advanced search for users with various filter options. Accessible only by SUPER_ADMIN and ADMIN roles."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered user list"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Page<UserResponse>> findUsersWithFilters(UserFilter filter) {
        return ResponseEntity.ok(userService.findUsersWithFilters(filter));
    }
      @Operation(
        summary = "Get user by ID", 
        description = "Retrieves a specific user by their ID. Users can view their own profiles. Admins can view any profile."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN') or @userSecurityService.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }    @Operation(
        summary = "Create new user", 
        description = "Creates a new user with specified role. Only SUPER_ADMIN can create admin users."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
      @Operation(
        summary = "Register new user", 
        description = "Public endpoint for user self-registration. Creates a new user with default permissions."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation error", content = @Content),
        @ApiResponse(responseCode = "409", description = "Username or email already exists", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        // Validate that passwords match
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new InvalidRequestException("Password and confirmation do not match");
        }
        
        // Check if username already exists
        if (userService.existsByUsername(registrationDTO.getUsername())) {
            throw new InvalidRequestException("Username is already taken");
        }
        
        // Check if email already exists
        if (userService.existsByEmail(registrationDTO.getEmail())) {
            throw new InvalidRequestException("Email is already registered");
        }
        
        // Create user entity from DTO
        User newUser = new User();
        newUser.setUsername(registrationDTO.getUsername());
        newUser.setFirstName(registrationDTO.getFirstName());
        newUser.setLastName(registrationDTO.getLastName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(registrationDTO.getPassword()); // Service will encrypt it
        newUser.setPhoneNumber(registrationDTO.getPhoneNumber());
        newUser.setIsActive(true);
        newUser.setIsEmailVerified(false);
        
        // Register the user (service assigns appropriate role)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.register(newUser));
    }
      @Operation(
        summary = "Update user", 
        description = "Updates a user's information. SUPER_ADMIN can update any user. ADMIN can only update non-admin users."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and @userSecurityService.isNonAdminUser(#id))")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user
    ) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
      @Operation(
        summary = "Delete user", 
        description = "Deletes a user from the system. SUPER_ADMIN can delete any user. ADMIN can only delete non-admin users."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and @userSecurityService.isNonAdminUser(#id))")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }    @Operation(
        summary = "Activate user", 
        description = "Activates a deactivated user account. SUPER_ADMIN can activate any user. ADMIN can only activate non-admin users."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User activated successfully"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and @userSecurityService.isNonAdminUser(#id))")
    public ResponseEntity<UserResponse> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activateUser(id));
    }
    
    @Operation(
        summary = "Deactivate user", 
        description = "Deactivates an active user account. SUPER_ADMIN can deactivate any user. ADMIN can only deactivate non-admin users."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and @userSecurityService.isNonAdminUser(#id))")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }
      @Operation(
        summary = "Reset user password", 
        description = "Resets a user's password and returns a temporary password. SUPER_ADMIN can reset any user's password. ADMIN can only reset non-admin users' passwords."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset successful"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('SUPER_ADMIN') or (hasRole('ADMIN') and @userSecurityService.isNonAdminUser(#id))")
    public ResponseEntity<UserResponse> resetUserPassword(@PathVariable Long id) {
        return ResponseEntity.ok(userService.resetPassword(id));
    }    @Operation(
        summary = "Update profile picture", 
        description = "Updates a user's profile picture. Users can update their own pictures. Admins can update any user's picture."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile picture updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file format or size", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/{id}/profile-picture")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or @userSecurityService.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> updateProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(userService.updateProfilePicture(id, file));
    }
      @Operation(
        summary = "Check if username exists", 
        description = "Checks if a username is already taken in the system. Used for validation during registration."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }
    
    @Operation(
        summary = "Check if email exists", 
        description = "Checks if an email address is already registered in the system. Used for validation during registration."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check completed successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
      @Operation(
        summary = "Get user profile", 
        description = "Retrieves a user's profile information. Users can view their own profiles. Admins can view any profile."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}/profile")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or @userSecurityService.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }
      
    @Operation(
        summary = "Update user password", 
        description = "Allows users to update their own password. Requires current password verification."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password data or passwords don't match", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}/password")
    @PreAuthorize("@userSecurityService.isCurrentUser(#id)")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeDTO passwordChangeDTO
    ) {
        // Check if passwords match
        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
            throw new InvalidRequestException("New password and confirmation do not match");
        }
        
        return ResponseEntity.ok(userService.updatePassword(
                id, 
                passwordChangeDTO.getCurrentPassword(), 
                passwordChangeDTO.getNewPassword()
        ));
    }

    @Operation(
        summary = "Reset user password", 
        description = "Resets a user's password and sends the new password via email. Accessible by SUPER_ADMIN, ADMIN, or the user themselves."
    )
    @ApiResponse(responseCode = "200", description = "Password reset successfully")
    @ApiResponse(responseCode = "403", description = "User is not authorized to reset this password")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN') or @userSecurityService.isCurrentUser(#id)")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password has been reset and sent to user's email");
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Request password reset by email", 
        description = "Sends a password reset link to the user's email address. This endpoint is publicly accessible."
    )
    @ApiResponse(responseCode = "200", description = "Password reset email sent successfully")
    @ApiResponse(responseCode = "404", description = "Email not found")
    @PostMapping("/request-password-reset")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestParam String email) {
        userService.requestPasswordReset(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "If the email exists in our system, a password reset link has been sent");
        return ResponseEntity.ok(response);
    }
}
