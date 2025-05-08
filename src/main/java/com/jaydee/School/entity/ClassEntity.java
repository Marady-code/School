package com.jaydee.School.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "classes")
public class ClassEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "class_id")
    private Long id;

	@NotBlank(message = "Class name is required")
	@Column(name = "class_name", nullable = false)
    private String className;

	@NotBlank(message = "Academic year is required")
	@Column(name = "academic_year", nullable = false)
    private String academicYear;

	@NotBlank(message = "Term is required")
	@Column(name = "term", nullable = false)
    private String term;

	@NotNull(message = "Capacity is required")
	@Min(value = 1, message = "Capacity must be at least 1")
	@Column(name = "capacity", nullable = false)
    private Integer capacity;

	@Column(name = "room_number")
    private String roomNumber;

	@Column(name = "description")
    private String description;
    
    @Column(name = "schedule")
    private String schedule;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "classEntity")
    private List<Student> students;
    
    @ManyToMany
    @JoinTable(
        name = "class_subjects",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;
    
    @OneToMany(mappedBy = "classEntity")
    private List<TimeTable> timeTables;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
