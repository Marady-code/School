package com.jaydee.School.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.AdminUserCreationDTO;
import com.jaydee.School.DTO.FirstLoginPasswordChangeDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.Exception.ResourceNotFound;
import com.jaydee.School.entity.User;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
@Tag(name = "User Enrollment", description = "APIs for secure user enrollment")
@SecurityRequirement(name = "JWT")
public class EnrollmentController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Operation(
        summary = "Create new user by admin", 
        description = "Creates a new user account for a student, parent, or teacher. Generates a temporary password and can send credentials via email."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/users")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody AdminUserCreationDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUserByAdmin(userDTO));
    }

    @Operation(
        summary = "Change password on first login", 
        description = "Allows a user to change their temporary password when logging in for the first time"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password or validation error", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/password-change/{userId}")
    public ResponseEntity<UserResponse> changePasswordFirstLogin(
            @PathVariable Long userId,
            @Valid @RequestBody FirstLoginPasswordChangeDTO passwordChangeDTO) {
        return ResponseEntity.ok(userService.changePasswordFirstLogin(userId, passwordChangeDTO));
    }

    @Operation(
        summary = "Check if password change is required", 
        description = "Checks if a user needs to change their password (for first login)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/password-change-required/{userId}")
    public ResponseEntity<Boolean> isPasswordChangeRequired(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.isPasswordChangeRequired(userId));
    }

    @Operation(
        summary = "Create student account", 
        description = "Creates a new student account with appropriate role. Admin access only."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/students")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> createStudentAccount(@Valid @RequestBody AdminUserCreationDTO userDTO) {
        // Set role to STUDENT explicitly
        userDTO.setRole("STUDENT");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUserByAdmin(userDTO));
    }

    @Operation(
        summary = "Create teacher account", 
        description = "Creates a new teacher account with appropriate role. Admin access only."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Teacher account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/teachers")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> createTeacherAccount(@Valid @RequestBody AdminUserCreationDTO userDTO) {
        // Set role to TEACHER explicitly
        userDTO.setRole("TEACHER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUserByAdmin(userDTO));
    }

    @Operation(
        summary = "Create parent account", 
        description = "Creates a new parent account with appropriate role. Admin access only."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Parent account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "403", description = "Permission denied", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/parents")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<UserResponse> createParentAccount(@Valid @RequestBody AdminUserCreationDTO userDTO) {
        // Set role to PARENT explicitly
        userDTO.setRole("PARENT");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUserByAdmin(userDTO));
    }
    
    @Operation(
        summary = "Print user credentials", 
        description = "Generates a printable format of user credentials for physical distribution"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credentials prepared for printing"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/print-credentials/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<String> printUserCredentials(@PathVariable Long userId) {
        // This is a placeholder - in a real implementation, this would generate a PDF or print-friendly format
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFound("User", userId));
        
        String credentials = "CREDENTIALS INFORMATION\n" + 
                "Username: " + user.getUsername() + "\n" +
                "Email: " + user.getEmail() + "\n" +
                "Temporary Password: <Cannot display for security reasons>";
        
        return ResponseEntity.ok(credentials);
    }
}
