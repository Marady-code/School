package com.jaydee.School.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.ClassEntity;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long>{

}
