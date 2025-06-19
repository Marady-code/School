package com.jaydee.School.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
	@Column(name = "teacher_id")
	private Long id;

	@JsonManagedReference
	@MapsId
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "teacher_id", referencedColumnName = "user_id")
	private User user;

	@NotBlank(message = "Gender is required")
	@Enumerated(EnumType.STRING)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dob;

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

	@JsonBackReference
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<ClassEntity> classes;

	@JsonBackReference
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	private List<Subject> subjects;

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

//	@Version
//	private long version;

}
