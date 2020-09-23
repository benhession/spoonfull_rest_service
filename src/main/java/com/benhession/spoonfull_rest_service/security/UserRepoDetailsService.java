//package com.benhession.spoonfull_rest_service.security;
//
//import com.benhession.spoonfull_rest_service.data.UserRepository;
//import com.benhession.spoonfull_rest_service.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class UserRepoDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserRepoDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
//
//        return user.orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", username)));
//    }
//}
