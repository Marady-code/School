package com.jaydee.School.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student>{
//	void DeleteById(@Param("id") Long id);

	// Changed from findByClassName to findByClassEntity_ClassName
	List<Student> findByClassEntity_ClassName(String className);
}
