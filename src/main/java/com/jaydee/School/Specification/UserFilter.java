package com.jaydee.School.Specification;

import com.jaydee.School.entity.Role;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class UserFilter {
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private Boolean active;
    
    // Pagination parameters
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "createdAt";
    private Sort.Direction direction = Sort.Direction.DESC;
} 