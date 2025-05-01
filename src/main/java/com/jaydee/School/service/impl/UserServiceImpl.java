//package com.jaydee.School.service.impl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.jaydee.School.DTO.LoginDTO;
//import com.jaydee.School.DTO.UserDTO;
//import com.jaydee.School.Specification.UserFilter;
//import com.jaydee.School.Specification.UserSpec;
//import com.jaydee.School.entity.User;
//import com.jaydee.School.repository.UserRepository;
//import com.jaydee.School.service.UserService;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//@Service
//public class UserServiceImpl implements UserService {
//
//	private final UserRepository userRepository;
////	private final UserMapper userMapper;
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	
//	@Override
//	public UserDTO login(LoginDTO loginDTO) {
//		User user = userRepository.findByUsername(loginDTO.getUsername())
//			.orElseThrow(() -> new RuntimeException("User not found"));
//
//		if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
//			throw new RuntimeException("Invalid username or password");
//		}
//
//		return convertToDTO(user);
//	}
//
//	@Override
//	public UserDTO getUserByUsername(String username) {
//		User user = userRepository.findByUsername(username)
//			.orElseThrow(() -> new RuntimeException("User not found"));
//		return convertToDTO(user);
//	}
//
//	@Override
//	public User createUser(User user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		return userRepository.save(user);
//	}
//
//	@Override
//	public User updateUser(Long id, User user) {
//		User existingUser = userRepository.findById(id)
//			.orElseThrow(() -> new RuntimeException("User not found"));
//
//		existingUser.setUsername(user.getUsername());
//		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
//		}
//		existingUser.setPhoneNumber(user.getPhoneNumber());
//		//existingUser.setRole(user.getRole());
//		
//		return userRepository.save(existingUser);
//	}
//
//	@Override
//	public void deleteUser(Long id) {
//		userRepository.deleteById(id);
//	}
//
//	@Override
//	public User getUserById(Long id) {
//		return userRepository.findById(id)
//			.orElseThrow(() -> new RuntimeException("User not found"));
//	}
//
//	@Override
//	public boolean existsByUsername(String username) {
//		return userRepository.existsByUsername(username);
//	}
//
//	private UserDTO convertToDTO(User user) {
//		UserDTO dto = new UserDTO();
//		dto.setId(user.getId());
//		dto.setUsername(user.getUsername());
//		dto.setPhoneNumber(user.getPhoneNumber());
//		//dto.setRole(user.getRole());
//		return dto;
//	}
//
//	@Override
//	public List<User> getAllUser() {
//		return userRepository.findAll();
//	}
//
//	public Page<User> findUsersWithFilters(UserFilter filter, Pageable pageable) {
//		Specification<User> spec = UserSpec.withFilters(
//			filter.getUsername(),
//			filter.getPhoneNumber(),
//			filter.getRole()
//		);
//		return userRepository.findAll(spec, pageable);
//	}
//
//}
