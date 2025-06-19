package com.jaydee.School.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "students")
public class Student {
	@Id
	@Column(name = "student_id")
	private Long id;

	@JsonManagedReference
	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id", referencedColumnName = "user_id")
	private User user;

	@NotNull(message = "Gender is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dob;

	@Column(name = "address")
	private String address;

	@Column(name = "emergency_contact")
	private String emergencyContact;

	@Column(name = "emergency_phone")
	private String emergencyPhone;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", updatable = true)
	private ClassEntity classEntity;

	@JsonBackReference
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attendance> attendanceRecords;

	@JsonBackReference
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExamResult> examResults;

	@JsonBackReference
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PerformanceReport> performanceReports;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	public enum Gender {
		MALE, FEMALE, OTHER
	}
}
