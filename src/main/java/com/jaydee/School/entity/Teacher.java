package com.jaydee.School.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
	private long id;
	
	@Column(name = "teacher_name", nullable = false)
	@NotNull(message = "Name is required")
	private String name;
	
	@Column(name = "date_of_birth")
	private LocalDate dob;
	
	private String subject;
	
	@Column(name = "teacher_phone")
	private String phoneNumber;
	
	@Column(name = "join_date")
	private LocalDate joinDate;
	
	@OneToMany(mappedBy = "teacher")
	private  List<Student> students;
	
    @OneToMany(mappedBy = "teacher")
    private List<ClassEntity> classes;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
	
//	@Version
//	private long version;
	
}
