package com.jaydee.School.service;

import com.jaydee.School.entity.Parent;
import com.jaydee.School.entity.Student;
import com.jaydee.School.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentService {
    
    @Autowired
    private ParentRepository parentRepository;

    public Parent createParent(Parent parent) {
        return parentRepository.save(parent);
    }

    public Parent updateParent(Long id, Parent parent) {
        Parent existingParent = parentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parent not found"));
        
        existingParent.setRelationship(parent.getRelationship());
        existingParent.setOccupation(parent.getOccupation());
        existingParent.setAddress(parent.getAddress());
        existingParent.setEmergencyContact(parent.getEmergencyContact());
        
        return parentRepository.save(existingParent);
    }

    public List<Parent> getParentsByStudent(Student student) {
        return parentRepository.findByStudents(student);
    }

    public void deleteParent(Long id) {
        parentRepository.deleteById(id);
    }
} 