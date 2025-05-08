package com.jaydee.School.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "teachers")
public class Teacher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "teacher_id")
	private Long id;
	
	@NotBlank(message = "First name is required")
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@NotBlank(message = "Gender is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;
	
	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dob;
	
	@Email(message = "Invalid email format")
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "qualification")
	private String qualification;
	
	@Column(name = "specialization")
	private String specialization;
	
	@Column(name = "experience_years")
	private Integer experienceYears;
	
	@Column(name = "join_date", nullable = false)
	private LocalDate joinDate;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;
	
	@ManyToMany
	@JoinTable(
		name = "teacher_subjects",
		joinColumns = @JoinColumn(name = "teacher_id"),
		inverseJoinColumns = @JoinColumn(name = "subject_id")
	)
	private List<Subject> subjects;
	
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Student> students;
	
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<ClassEntity> classes;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	public enum Gender {
		MALE, FEMALE, OTHER
	}
	
//	@Version
//	private long version;
	
}
