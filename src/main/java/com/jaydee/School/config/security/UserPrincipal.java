//package com.jaydee.School.config.security;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.jaydee.School.entity.Permission;
//import com.jaydee.School.entity.User;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@Getter
//public class UserPrincipal implements UserDetails {
//    private String username;
//    private String password;
//    private Collection<? extends GrantedAuthority> authorities;
//
//    public static UserPrincipal create(User user) {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        
//        // Add role-based authorities (ROLE_ADMIN, ROLE_TEACHER, etc.)
//        user.getRoles().forEach(role -> 
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
//        );
//        
//        // Add all permissions from all roles
//        Set<String> permissions = user.getRoles().stream()
//            .flatMap(role -> role.getPermissions().stream())
//            .map(Permission::getName)
//            .collect(Collectors.toSet());
//            
//        // Add permission-based authorities
//        permissions.forEach(permission -> 
//            authorities.add(new SimpleGrantedAuthority(permission))
//        );
//
//        return new UserPrincipal(
//            user.getUsername(),
//            user.getPassword(),
//            authorities
//        );
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}