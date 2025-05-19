package com.jaydee.School.service;

import java.util.List;

import com.jaydee.School.entity.Parent;
import com.jaydee.School.entity.Student;

public interface ParentService {
     Parent createParent(Parent parent);    Parent updateParent(Long id, Parent parent);

    List<Parent> getParentsByStudent(Student student);
    
    void deleteParent(Long id);
} 