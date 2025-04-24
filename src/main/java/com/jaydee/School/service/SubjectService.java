package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.SubjectDTO;
import com.jaydee.School.Exception.ResourceNotFoundException;

public interface SubjectService {
    /**
     * Create a new subject
     * @param subjectDTO Subject data
     * @return Created subject
     * @throws IllegalArgumentException if subject code already exists
     */
    SubjectDTO createSubject(SubjectDTO subjectDTO);

    /**
     * Update an existing subject
     * @param id Subject ID
     * @param subjectDTO Updated subject data
     * @return Updated subject
     * @throws ResourceNotFoundException if subject not found
     */
    SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO);

    /**
     * Get subjects by academic year and term
     * @param academicYear Academic year
     * @param term Term
     * @return List of subjects
     */
    List<SubjectDTO> getSubjectsByAcademicYearAndTerm(String academicYear, String term);

    /**
     * Delete a subject
     * @param id Subject ID
     * @throws ResourceNotFoundException if subject not found
     */
    void deleteSubject(Long id);
    
    /**
     * Get subject by ID
     * @param id Subject ID
     * @return Subject
     * @throws ResourceNotFoundException if subject not found
     */
    SubjectDTO getSubjectById(Long id);

    List<SubjectDTO> getAllSubjects();

    boolean existsByCode(String code);
} 