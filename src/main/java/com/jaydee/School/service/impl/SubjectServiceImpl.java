package com.jaydee.School.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.SubjectDTO;
import com.jaydee.School.Exception.ResourceNotFoundException;
import com.jaydee.School.entity.Subject;
import com.jaydee.School.mapper.SubjectMapper;
import com.jaydee.School.repository.SubjectRepository;
import com.jaydee.School.service.SubjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    @Transactional
    public SubjectDTO createSubject(SubjectDTO subjectDTO) {
        // Validate subject code uniqueness
        if (subjectRepository.existsByCode(subjectDTO.getCode())) {
            throw new IllegalArgumentException("Subject code already exists: " + subjectDTO.getCode());
        }

        Subject subject = subjectMapper.toEntity(subjectDTO);
        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.toDTO(savedSubject);
    }
    @Override
    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) {
        Subject existingSubject = subjectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));

        // Check if new code conflicts with existing subjects
        if (!existingSubject.getCode().equals(subjectDTO.getCode()) && 
            subjectRepository.existsByCode(subjectDTO.getCode())) {
            throw new IllegalArgumentException("Subject code already exists: " + subjectDTO.getCode());
        }

        Subject updatedSubject = subjectMapper.toEntity(subjectDTO);
        updatedSubject.setId(id);
        Subject savedSubject = subjectRepository.save(updatedSubject);
        return subjectMapper.toDTO(savedSubject);
    }

    @Override
    public List<SubjectDTO> getSubjectsByAcademicYearAndTerm(String academicYear, String term) {
        List<Subject> subjects = subjectRepository.findByAcademicYearAndTerm(academicYear, term);
        return subjectMapper.toDTOList(subjects);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject", "id", id);
        }
        subjectRepository.deleteById(id);
    }

    @Override
    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));
        return subjectMapper.toDTO(subject);
    }

    @Override
    public List<SubjectDTO> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjectMapper.toDTOList(subjects);
    }

    @Override
    public boolean existsByCode(String code) {
        return subjectRepository.existsByCode(code);
    }
} 