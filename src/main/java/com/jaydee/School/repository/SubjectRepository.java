package com.jaydee.School.repository;

import com.jaydee.School.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByAcademicYearAndTerm(String academicYear, String term);
    Optional<Subject> findByCode(String code);
    boolean existsByCode(String code);
    List<Subject> findByNameContainingIgnoreCase(String name);
    List<Subject> findByCodeContainingIgnoreCase(String code);
} 