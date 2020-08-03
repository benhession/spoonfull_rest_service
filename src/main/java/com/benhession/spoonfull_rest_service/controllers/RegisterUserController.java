package com.benhession.spoonfull_rest_service.controllers;

import com.benhession.spoonfull_rest_service.data.UserRepository;
import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.security.UserDetailsForm;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/register")
@CrossOrigin("*")
public class RegisterUserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody UserDetailsForm form) {
        return userRepository.save(form.toUser(passwordEncoder));
    }

    @GetMapping(value = "/csrf-token")
    public @ResponseBody String getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        response.addHeader("_csrf", token.getToken());
        response.addHeader("_csrf_header", token.getHeaderName());
        return "";
    }
}
