package com.benhession.spoonfull_rest_service.security;

import com.benhession.spoonfull_rest_service.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserDetailsForm {
    private String username;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(this.getUsername(), passwordEncoder.encode(this.getPassword()));
    }
}
