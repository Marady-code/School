package com.jaydee.School.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaydee.School.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleName name);
    boolean existsByName(Role.RoleName name);
}
