package com.jaydee.School.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@Column(name = "capacity")
    private Integer capacity;

	@Column(name = "room_number")
    private String roomNumber;

	@Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "classEntity")
    private List<Student> students;
}
