package com.jaydee.School.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaydee.School.DTO.LoginDTO;
import com.jaydee.School.DTO.UserDTO;
import com.jaydee.School.entity.User;

@Service
public interface UserService {

    User createUser(User user);
    
    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User getUserById(Long id);
    
    UserDTO login(LoginDTO loginDTO);
    
    List<User> getAllUser();

    UserDTO getUserByUsername(String username);

    boolean existsByUsername(String username);


}