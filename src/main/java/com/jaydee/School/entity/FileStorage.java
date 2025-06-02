package com.jaydee.School.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_storage")
public class FileStorage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    
    @NotBlank(message = "File name is required")
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @NotBlank(message = "File type is required")
    @Column(name = "file_type", nullable = false)
    private String fileType;
    
    @NotNull(message = "File size is required")
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @NotBlank(message = "File path is required")
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "file_category", nullable = false)
    private FileCategory fileCategory;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne(mappedBy = "profilePicture")
    private User profileUser;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum FileCategory {
        PROFILE_PICTURE,
        DOCUMENT,
        CERTIFICATE,
        OTHER
    }
}