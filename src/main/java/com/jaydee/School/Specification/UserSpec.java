package com.jaydee.School.Specification;

import com.jaydee.School.config.security.Role;
import com.jaydee.School.entity.User;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpec {
    
    public static Specification<User> withFilters(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + filter.getUsername().toLowerCase() + "%"));
            }
            
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
            }
            
            if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isEmpty()) {
                predicates.add(cb.like(root.get("phoneNumber"), "%" + filter.getPhoneNumber() + "%"));
            }
            
            if (filter.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), filter.getRole()));
            }
            
            if (filter.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), filter.getActive()));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    public static Specification<User> withEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }
    
    public static Specification<User> withRole(Role role) {
        return (root, query, cb) -> {
            if (role == null) {
                return null;
            }
            return cb.equal(root.get("role"), role);
        };
    }
    
    public static Specification<User> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) {
                return null;
            }
            return cb.equal(root.get("active"), active);
        };
    }
} 