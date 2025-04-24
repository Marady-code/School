package com.jaydee.School.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private long id;

	@NotNull(message = "First name is required")
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@NotNull(message = "Last name is required")
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotBlank(message = "Gender is required")
	@Column(name = "gender", nullable = false)
	private String gender;

//    @DateTimeFormat(pattern = "dd-MM-yyyy") // ✅ This helps when binding from forms
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dob;
	
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;
	
	@OneToMany(mappedBy = "student" ,cascade = CascadeType.ALL, orphanRemoval = true)//orphanRemoval = When Student is removed, all their attendance records are deleted
	@Column(name = "student_attendances")
	private List<Attendance> attendanceRecords;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
	
}
