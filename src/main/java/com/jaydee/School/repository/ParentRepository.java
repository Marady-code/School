package com.jaydee.School.repository;

import com.jaydee.School.entity.Parent;
import com.jaydee.School.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    List<Parent> findByStudents(Student student);
} 