package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.DTO.SubjectDTO;

public interface SubjectService {

    SubjectDTO createSubject(SubjectDTO subjectDTO);

    SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO);

    List<SubjectDTO> getSubjectsByAcademicYearAndTerm(String academicYear, String term);

    void deleteSubject(Long id);

    SubjectDTO getSubjectById(Long id);

    List<SubjectDTO> getAllSubjects();

    boolean existsByCode(String code);

    List<SubjectDTO> getSubjectsByTeacher(Long teacherId);
}