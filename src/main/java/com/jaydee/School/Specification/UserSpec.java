package com.jaydee.School.Specification;

import com.jaydee.School.entity.User;
import com.jaydee.School.entity.Role;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpec {
    
    public static Specification<User> withFilters(String username, String phoneNumber, Role role) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (username != null && !username.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }
            
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                predicates.add(cb.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
            }
            
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    public static Specification<User> withUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
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
} 