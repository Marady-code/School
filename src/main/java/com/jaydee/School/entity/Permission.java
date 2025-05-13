package com.jaydee.School.entity;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
@EqualsAndHashCode(exclude = {"roles"})
@ToString(exclude = {"roles"})
public class Permission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PermissionName name;

    @Column
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;    public enum PermissionName {
        CREATE_USER,
        READ_USER,
        UPDATE_USER,
        DELETE_USER,
        CREATE_ADMIN,
        READ_ADMIN,
        UPDATE_ADMIN,
        DELETE_ADMIN,
        CREATE_TEACHER,
        READ_TEACHER,
        UPDATE_TEACHER,
        DELETE_TEACHER,
        CREATE_STUDENT,
        READ_STUDENT,
        UPDATE_STUDENT,
        DELETE_STUDENT,
        MANAGE_ROLES,
        MANAGE_PERMISSIONS,
        VIEW_REPORTS,
        MANAGE_SYSTEM
    }
    
    // Explicit getter and setter methods
    public PermissionName getName() {
        return name;
    }
    
    public void setName(PermissionName name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}