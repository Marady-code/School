package com.jaydee.School.DTO;

import com.jaydee.School.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
	
	private long id;
	
	private String username;
	
	private String phoneNumber;
	
	private Role role;

}
