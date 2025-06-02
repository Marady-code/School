package com.jaydee.School.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserCreationDTO {
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Username is required")
    @Size(min = 4, message = "Username must be at least 4 characters")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;
    
    private String phoneNumber;
    
    @NotBlank(message = "Role is required")
    private String role; // STUDENT, PARENT, TEACHER, ADMIN
    
    // Additional fields specific to each role
    // For Student
    private String grade;
    private String section;
    private String studentId;
    
    // For Teacher
    private String specialization;
    private String employeeId;
    
    // For Parent
    private Long[] childrenIds; // IDs of student children
    
    // For printing or emailing credentials
    private boolean sendCredentialsEmail;
    private boolean printCredentials;
}
