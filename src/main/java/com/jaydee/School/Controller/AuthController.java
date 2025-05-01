//package com.jaydee.School.Controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jaydee.School.DTO.ApiResponse;
//import com.jaydee.School.DTO.LoginDTO;
//import com.jaydee.School.DTO.UserDTO;
//import com.jaydee.School.entity.User;
//import com.jaydee.School.service.UserService;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//	private final UserService userService;
//
//	@PostMapping("/login")
//	public ResponseEntity<ApiResponse<UserDTO>> login(@Valid @RequestBody LoginDTO loginDTO) {
//		UserDTO userDTO = userService.login(loginDTO);
//		return ResponseEntity.ok(ApiResponse.success("Login successful", userDTO));
//	}
//
//	@PostMapping("/register")
//	public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody User user) {
//		if (userService.existsByUsername(user.getUsername())) {
//			return ResponseEntity.badRequest().body(ApiResponse.error("Username is already taken"));
//		}
//
////        if (userService.existsByEmail(user.getEmail())) {
////            return ResponseEntity
////                .badRequest()
////                .body(new MessageResponse("Email is already in use!"));
////        }
//
//		User createdUser = userService.createUser(user);
//		UserDTO userDTO = userService.getUserByUsername(createdUser.getUsername());
//		return ResponseEntity.status(HttpStatus.CREATED)
//				.body(ApiResponse.success("User registered successfully", userDTO));
//	}
//
//	@PostMapping("/logout")
//	public ResponseEntity<ApiResponse<Void>> logout() {
//		// Implement logout logic here if using session-based authentication
//		return ResponseEntity.ok(ApiResponse.success("Logged out successfully"));
//	}
//}
