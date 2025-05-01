package com.jaydee.School.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findAllByNameIn(List<String> names);
}
