//package com.jaydee.School.config.security;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
//public enum RoleEnum {
//
//    ADMIN(Set.of(
//        STUDENT_READ, STUDENT_WRITE,
//        TEACHER_READ, TEACHER_WRITE,
//        PARENT_READ, PARENT_WRITE,
//        CLASS_READ, CLASS_WRITE,
//        SUBJECT_READ, SUBJECT_WRITE,
//        ATTENDANCE_READ, ATTENDANCE_WRITE,
//        EXAM_RESULT_READ, EXAM_RESULT_WRITE,
//        REPORT_READ, REPORT_WRITE,
//        TIMETABLE_READ, TIMETABLE_WRITE,
//        USER_READ, USER_WRITE
//    )),
//
//    TEACHER(Set.of(
//        STUDENT_READ,
//        CLASS_READ,
//        SUBJECT_READ,
//        ATTENDANCE_READ, ATTENDANCE_WRITE,
//        EXAM_RESULT_READ, EXAM_RESULT_WRITE,
//        REPORT_READ,
//        TIMETABLE_READ
//    )),
//
//    STUDENT(Set.of(
//        CLASS_READ,
//        SUBJECT_READ,
//        ATTENDANCE_READ,
//        EXAM_RESULT_READ,
//        REPORT_READ,
//        TIMETABLE_READ
//    )),
//
//    PARENT(Set.of(
//        STUDENT_READ,
//        ATTENDANCE_READ,
//        EXAM_RESULT_READ,
//        REPORT_READ,
//        TIMETABLE_READ
//    ));
//
//    private final Set<PermissionEnum> permissions;
//    
//    public Set<SimpleGrantedAuthority> getAuthorities(){
//    	Set<SimpleGrantedAuthority> grantedAuthorities = this.permissions.stream()
//    			.map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
//    			.collect(Collectors.toSet());
//    	
//    	SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_"+this.name());
//    	grantedAuthorities.add(role);
//    	System.out.println(grantedAuthorities);
//    	return grantedAuthorities;
//    }
//    
//}
