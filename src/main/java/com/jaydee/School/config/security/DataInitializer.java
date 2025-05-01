//package com.jaydee.School.config.security;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import com.jaydee.School.entity.Permission;
//import com.jaydee.School.entity.Role;
//import com.jaydee.School.entity.User;
//import com.jaydee.School.repository.PermissionRepository;
//import com.jaydee.School.repository.RoleRepository;
//import com.jaydee.School.repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DataInitializer implements CommandLineRunner {
//
//    private final PermissionRepository permissionRepository;
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) {
//        // Only initialize if no permissions exist
//        if (permissionRepository.count() == 0) {
//            initializePermissions();
//        }
//        
//        // Only initialize if no roles exist
//        if (roleRepository.count() == 0) {
//            initializeRoles();
//        }
//        
//        // Create admin user if it doesn't exist
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            createAdminUser();
//        }
//    }
//    
//    private void initializePermissions() {
//        log.info("Initializing permissions...");
//        
//        List<Permission> permissions = Arrays.asList(
//            createPermission("student:read"),
//            createPermission("student:write"),
//            createPermission("teacher:read"),
//            createPermission("teacher:write"),
//            createPermission("parent:read"),
//            createPermission("parent:write"),
//            createPermission("class:read"),
//            createPermission("class:write"),
//            createPermission("subject:read"),
//            createPermission("subject:write"),
//            createPermission("attendance:read"),
//            createPermission("attendance:write"),
//            createPermission("exam_result:read"),
//            createPermission("exam_result:write"),
//            createPermission("report:read"),
//            createPermission("report:write"),
//            createPermission("timetable:read"),
//            createPermission("timetable:write"),
//            createPermission("user:read"),
//            createPermission("user:write")
//        );
//        
//        permissionRepository.saveAll(permissions);
//        log.info("Permissions initialized successfully");
//    }
//    
//    private Permission createPermission(String name) {
//        return Permission.builder()
//                .name(name)
//                .build();
//    }
//    
//    private void initializeRoles() {
//        log.info("Initializing roles...");
//        
//        // Admin role with all permissions
//        Role adminRole = Role.builder()
//                .name("ADMIN")
//                .permissions(new HashSet<>(permissionRepository.findAll()))
//                .build();
//        
//        // Teacher role with specific permissions
//        Set<Permission> teacherPermissions = new HashSet<>(permissionRepository.findAllByNameIn(Arrays.asList(
//            "student:read",
//            "class:read",
//            "subject:read",
//            "attendance:read", "attendance:write",
//            "exam_result:read", "exam_result:write",
//            "report:read",
//            "timetable:read"
//        )));
//        
//        Role teacherRole = Role.builder()
//                .name("TEACHER")
//                .permissions(teacherPermissions)
//                .build();
//        
//        // Student role with specific permissions
//        Set<Permission> studentPermissions = new HashSet<>(permissionRepository.findAllByNameIn(Arrays.asList(
//            "class:read",
//            "subject:read",
//            "attendance:read",
//            "exam_result:read",
//            "report:read",
//            "timetable:read"
//        )));
//        
//        Role studentRole = Role.builder()
//                .name("STUDENT")
//                .permissions(studentPermissions)
//                .build();
//        
//        // Parent role with specific permissions
//        Set<Permission> parentPermissions = new HashSet<>(permissionRepository.findAllByNameIn(Arrays.asList(
//            "student:read",
//            "attendance:read",
//            "exam_result:read",
//            "report:read",
//            "timetable:read"
//        )));
//        
//        Role parentRole = Role.builder()
//                .name("PARENT")
//                .permissions(parentPermissions)
//                .build();
//        
//        roleRepository.saveAll(Arrays.asList(adminRole, teacherRole, studentRole, parentRole));
//        log.info("Roles initialized successfully");
//    }
//    
//    private void createAdminUser() {
//        log.info("Creating admin user...");
//        
//        Role adminRole = roleRepository.findByName("ADMIN")
//            .orElseThrow(() -> new RuntimeException("Admin role not found"));
//        
//        User adminUser = User.builder()
//            .username("admin")
//            .password(passwordEncoder.encode("admin123"))
//            .roles(Set.of(adminRole))
//            .phoneNumber("1234567890")
//            .build();
//        
//        userRepository.save(adminUser);
//        log.info("Admin user created successfully");
//    }
//}