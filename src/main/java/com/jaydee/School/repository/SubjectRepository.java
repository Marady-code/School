package com.jaydee.School.repository;

import com.jaydee.School.entity.Subject;
import com.jaydee.School.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    // Existing methods with improvements
    List<Subject> findByAcademicYearAndTerm(String academicYear, String term);
    Optional<Subject> findByCode(String code);
    boolean existsByCode(String code);
    List<Subject> findByNameContainingIgnoreCase(String name);
    List<Subject> findByCodeContainingIgnoreCase(String code);
    List<Subject> findByTeacher(Teacher teacher);
    
    // New query methods
    @Query("SELECT s FROM Subject s WHERE s.teacher.id = :teacherId")
    List<Subject> findAllByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT s FROM Subject s " +
           "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:code IS NULL OR LOWER(s.code) LIKE LOWER(CONCAT('%', :code, '%'))) " +
           "AND (:academicYear IS NULL OR s.academicYear = :academicYear) " +
           "AND (:term IS NULL OR s.term = :term)")
    Page<Subject> searchSubjects(
        @Param("name") String name,
        @Param("code") String code,
        @Param("academicYear") String academicYear,
        @Param("term") String term,
        Pageable pageable
    );
    
    @Query("SELECT s FROM Subject s " +
           "LEFT JOIN FETCH s.teacher " +
           "WHERE s.id = :id")
    Optional<Subject> findByIdWithDetails(@Param("id") Long id);
    
    @Query("SELECT COUNT(s) > 0 FROM Subject s " +
           "WHERE s.code = :code " +
           "AND (:id IS NULL OR s.id != :id)")
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);
    
    @Query("SELECT s FROM Subject s " +
           "WHERE s.teacher.id = :teacherId " +
           "AND s.academicYear = :academicYear " +
           "AND s.term = :term")
    List<Subject> findByTeacherAndAcademicYearAndTerm(
        @Param("teacherId") Long teacherId,
        @Param("academicYear") String academicYear,
        @Param("term") String term
    );
}