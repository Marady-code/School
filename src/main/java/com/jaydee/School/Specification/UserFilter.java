package com.jaydee.School.Specification;

import com.jaydee.School.entity.Role;
import lombok.Data;

@Data
public class UserFilter {
    private String username;
    private String phoneNumber;
    private Role role;
} 