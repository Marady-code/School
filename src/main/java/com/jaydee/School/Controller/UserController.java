package com.jaydee.School.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.UserDTO;
import com.jaydee.School.entity.User;
import com.jaydee.School.mapper.UserMapper;
import com.jaydee.School.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admins")
public class UserController {
	
	private final UserService adminService;
	private final UserMapper adminMapper;
	
	@PostMapping
	public ResponseEntity<?> create (@RequestBody UserDTO adminDTO){
		User user = adminMapper.toEntity(adminDTO);
		user = adminService.createUser(user);
		return ResponseEntity.ok(adminMapper.toUserDTO(user));
	}
	
	
	
}
