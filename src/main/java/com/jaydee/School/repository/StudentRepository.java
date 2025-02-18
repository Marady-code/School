package com.jaydee.School.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

}
