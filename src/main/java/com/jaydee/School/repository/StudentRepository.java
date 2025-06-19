package com.jaydee.School.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
//	void DeleteById(@Param("id") Long id);

	// Changed from findByClassName to findByClassEntity_ClassName
	List<Student> findByClassEntity_ClassName(String className);

	@Query("SELECT s FROM Student s WHERE s.user.phoneNumber = :phoneNumber")
	Optional<Student> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	@Query("SELECT s FROM Student s WHERE s.user.id = :userId")
	Optional<Student> findByUserId(@Param("userId") Long userId);

	@Query("SELECT s FROM Student s WHERE s.user.email = :email")
	Optional<Student> findByEmail(@Param("email") String email);
}
