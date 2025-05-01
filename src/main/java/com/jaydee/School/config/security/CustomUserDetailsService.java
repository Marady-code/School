//package com.jaydee.School.config.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.jaydee.School.entity.User;
//import com.jaydee.School.repository.UserRepository;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
//
//        return UserPrincipal.create(user);
//    }
//
////    @Transactional
////    public UserDetails loadUserById(Long id) {
////        User user = userRepository.findById(id)
////                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
////
////        return UserPrincipal.create(user);
////    }
//} 