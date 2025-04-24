package com.jaydee.School.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class TimeTableDTO {
    private Long id;

    @NotNull(message = "Class ID is required")
    private Long classId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotBlank(message = "Term is required")
    private String term;
} 