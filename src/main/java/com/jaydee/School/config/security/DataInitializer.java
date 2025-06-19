package com.jaydee.School.config.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jaydee.School.entity.Permission;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.User;
import com.jaydee.School.repository.PermissionRepository;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

	private final PermissionRepository permissionRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(String... args) {
		// Only initialize if no permissions exist
		if (permissionRepository.count() == 0) {
			initializePermissions();
		}

		// Only initialize if no roles exist
		if (roleRepository.count() == 0) {
			initializeRoles();
		}
		// Create default super admin user if it doesn't exist
		if (userRepository.findByEmail("superadmin@school.com").isEmpty()
				&& userRepository.findByUsername("superadmin").isEmpty()) {
			createSuperAdminUser();
		}

//        // Create default admin user if it doesn't exist
//        if (userRepository.findByEmail("admin@school.com").isEmpty() && 
//            userRepository.findByUsername("admin").isEmpty()) {
//            createAdminUser();
//        }

	}

	@Transactional
	private void initializePermissions() {
		log.info("Initializing permissions...");

		List<Permission> permissions = Arrays.asList(createPermission(Permission.PermissionName.CREATE_USER),
				createPermission(Permission.PermissionName.READ_USER),
				createPermission(Permission.PermissionName.UPDATE_USER),
				createPermission(Permission.PermissionName.DELETE_USER),
				createPermission(Permission.PermissionName.CREATE_ADMIN),
				createPermission(Permission.PermissionName.READ_ADMIN),
				createPermission(Permission.PermissionName.UPDATE_ADMIN),
				createPermission(Permission.PermissionName.DELETE_ADMIN),
				createPermission(Permission.PermissionName.CREATE_TEACHER),
				createPermission(Permission.PermissionName.READ_TEACHER),
				createPermission(Permission.PermissionName.UPDATE_TEACHER),
				createPermission(Permission.PermissionName.DELETE_TEACHER),
				createPermission(Permission.PermissionName.CREATE_STUDENT),
				createPermission(Permission.PermissionName.READ_STUDENT),
				createPermission(Permission.PermissionName.UPDATE_STUDENT),
				createPermission(Permission.PermissionName.DELETE_STUDENT),
				createPermission(Permission.PermissionName.MANAGE_ROLES),
				createPermission(Permission.PermissionName.MANAGE_PERMISSIONS),
				createPermission(Permission.PermissionName.VIEW_REPORTS),
				createPermission(Permission.PermissionName.MANAGE_SYSTEM));

		permissionRepository.saveAll(permissions);
		log.info("Permissions initialized successfully");
	}

	private Permission createPermission(Permission.PermissionName name) {
		Permission permission = new Permission();
		permission.setName(name);
		return permission;
	}

	@Transactional
	private void initializeRoles() {
		log.info("Initializing roles...");

		// Super Admin role with all permissions
		Role superAdminRole = new Role();
		superAdminRole.setName(Role.RoleName.SUPER_ADMIN);
		superAdminRole.setDescription("Super Administrator with full system access");
		superAdminRole.setPermissions(new HashSet<>(permissionRepository.findAll()));

		// Admin role with most permissions but not all system management
		Role adminRole = new Role();
		adminRole.setName(Role.RoleName.ADMIN);
		adminRole.setDescription("Administrator with limited system access");
		adminRole.setPermissions(new HashSet<>(permissionRepository.findAll().stream()
				.filter(p -> !p.getName().equals(Permission.PermissionName.MANAGE_SYSTEM))
				.collect(java.util.stream.Collectors.toSet())));

		// Teacher role with specific permissions
		Set<Permission> teacherPermissions = new HashSet<>(permissionRepository.findAll().stream()
				.filter(p -> Arrays.asList(Permission.PermissionName.READ_STUDENT,
						Permission.PermissionName.READ_TEACHER, Permission.PermissionName.UPDATE_TEACHER)
						.contains(p.getName()))
				.collect(java.util.stream.Collectors.toSet()));

		Role teacherRole = new Role();
		teacherRole.setName(Role.RoleName.TEACHER);
		teacherRole.setDescription("Teacher with student and self-management permissions");
		teacherRole.setPermissions(teacherPermissions);

		// Student role with specific permissions
		Set<Permission> studentPermissions = new HashSet<>(
				permissionRepository.findAll().stream()
						.filter(p -> Arrays.asList(Permission.PermissionName.READ_STUDENT,
								Permission.PermissionName.UPDATE_STUDENT).contains(p.getName()))
						.collect(java.util.stream.Collectors.toSet()));

		Role studentRole = new Role();
		studentRole.setName(Role.RoleName.STUDENT);
		studentRole.setDescription("Student with self-management permissions");
		studentRole.setPermissions(studentPermissions);

		// Parent role with specific permissions
		Set<Permission> parentPermissions = new HashSet<>(permissionRepository.findAll().stream()
				.filter(p -> Arrays.asList(Permission.PermissionName.READ_STUDENT).contains(p.getName()))
				.collect(java.util.stream.Collectors.toSet()));

		Role parentRole = new Role();
		parentRole.setName(Role.RoleName.PARENT);
		parentRole.setDescription("Parent with student viewing permissions");
		parentRole.setPermissions(parentPermissions);

		roleRepository.saveAll(Arrays.asList(superAdminRole, adminRole, teacherRole, studentRole, parentRole));
		log.info("Roles initialized successfully");
	}

	@Transactional
	private void createSuperAdminUser() {
		log.info("Creating super admin user...");

		Role superAdminRole = roleRepository.findByName(Role.RoleName.SUPER_ADMIN)
				.orElseThrow(() -> new RuntimeException("Super Admin role not found"));

		User superAdminUser = new User();
		superAdminUser.setFirstName("Super");
		superAdminUser.setLastName("Admin");
		superAdminUser.setEmail("superadmin@school.com");
		superAdminUser.setUsername("superadmin");
		superAdminUser.setPassword(passwordEncoder.encode("superadmin123"));
		superAdminUser.setRoles(Set.of(superAdminRole));
		superAdminUser.setRole("SUPER_ADMIN"); // Set the direct role field to match the role enum
		superAdminUser.setIsActive(true);

		userRepository.save(superAdminUser);
		log.info("Super Admin user created successfully");
	}

//    @Transactional
//    private void createAdminUser() {
//        log.info("Creating admin user...");
//        
//        Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
//            .orElseThrow(() -> new RuntimeException("Admin role not found"));
//        
//        User adminUser = new User();
//        adminUser.setFirstName("Admin");
//        adminUser.setLastName("User");
//        adminUser.setEmail("admin@school.com");
//        adminUser.setUsername("admin"); // Set username to be easier to remember
//        adminUser.setPassword(passwordEncoder.encode("admin123"));
//        adminUser.setRoles(Set.of(adminRole));
//        adminUser.setRole("ADMIN"); // Set the direct role field to match the role enum
//        adminUser.setIsActive(true);
//        adminUser.setIsEmailVerified(true);
//        
//        userRepository.save(adminUser);
//        log.info("Admin user created successfully");
//    }

}
