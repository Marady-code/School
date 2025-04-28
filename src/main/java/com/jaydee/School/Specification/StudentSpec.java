package com.jaydee.School.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.jaydee.School.entity.Student;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class StudentSpec implements Specification<Student>{
	
	private final StudentFilter studentFilter;
	
	List<Predicate> predicates = new ArrayList<>();

	@Override
	public Predicate toPredicate(Root<Student> student, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if(studentFilter.getFirstName() != null) {
			Predicate name = student.get("Name").in(studentFilter.getFirstName());
			predicates.add(name);
		}
		
//		if(studentFilter.getId() != null) {
//			Predicate id = student.get("id").in(studentFilter.getId());
//			predicates.add(id);
//		}
		return cb.and(predicates.toArray(Predicate[]::new));
	}
	
	public static Specification<Student> withFilters(String studentId, String firstName, String lastName) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			if (studentId != null && !studentId.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("studentId")), "%" + studentId.toLowerCase() + "%"));
			}
			
			if (firstName != null && !firstName.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
			}
			
			if (lastName != null && !lastName.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
			}
			
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public static Specification<Student> withStudentId(String studentId) {
		return (root, query, cb) -> {
			if (studentId == null || studentId.isEmpty()) {
				return null;
			}
			return cb.like(cb.lower(root.get("studentId")), "%" + studentId.toLowerCase() + "%");
		};
	}
	
	public static Specification<Student> withName(String firstName, String lastName) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			if (firstName != null && !firstName.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
			}
			
			if (lastName != null && !lastName.isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
			}
			
			return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
