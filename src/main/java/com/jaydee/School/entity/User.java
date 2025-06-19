package com.jaydee.School.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "role", nullable = false)
	private String role;
	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "is_first_login")
	private Boolean isFirstLogin = true;

	@Column(name = "password_change_required")
	private Boolean passwordChangeRequired = false;

	@Column(name = "last_password_change_date")
	private LocalDateTime lastPasswordChangeDate;

	@JsonManagedReference
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@JsonBackReference
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Student student;

	@JsonBackReference
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Teacher teacher;

	@JsonBackReference
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Parent parent;

	@JsonIgnore
	@Transient
	private String plainPassword;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
			if (role.getPermissions() != null) {
				role.getPermissions().forEach(
						permission -> authorities.add(new SimpleGrantedAuthority(permission.getName().name())));
			}
		});
		return authorities;
	}

	@Override
	public String getUsername() {
		return username; // Return the actual username field
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive;
	}

	public boolean hasRole(Role.RoleName roleName) {
		return roles.stream().anyMatch(role -> role.getName() == roleName);
	}

	public boolean hasPermission(Permission.PermissionName permissionName) {
		return roles.stream()
				.flatMap(role -> role.getPermissions() != null ? role.getPermissions().stream() : Stream.empty())
				.anyMatch(permission -> permission.getName() == permissionName);
	}

	public boolean isStudent() {
		return student != null;
	}

	public boolean isTeacher() {
		return teacher != null;
	}

	public boolean isParent() {
		return parent != null;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Boolean getPasswordChangeRequired() {
		return passwordChangeRequired;
	}

	public void setPasswordChangeRequired(Boolean passwordChangeRequired) {
		this.passwordChangeRequired = passwordChangeRequired;
	}

	public LocalDateTime getLastPasswordChangeDate() {
		return lastPasswordChangeDate;
	}

	public void setLastPasswordChangeDate(LocalDateTime lastPasswordChangeDate) {
		this.lastPasswordChangeDate = lastPasswordChangeDate;
	}
}