package com.jaydee.School.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    Set<Permission> findByNameIn(Set<String> names);
    boolean existsByName(String name);
    List<Permission> findAllByNameIn(List<String> names);
}
