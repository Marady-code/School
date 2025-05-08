package com.jaydee.School.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GradeDTO {
    private Long id;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score cannot exceed 100")
    private Double score;

    @NotBlank(message = "Letter grade is required")
    private String letterGrade;

    private String comments;

    private String gradingPeriod;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotBlank(message = "Term is required")
    private String term;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}