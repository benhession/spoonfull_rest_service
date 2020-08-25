package com.benhession.spoonfull_rest_service.controllers;

import com.benhession.spoonfull_rest_service.data.UserService;
import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.representation_models.UserModel;
import com.benhession.spoonfull_rest_service.representation_models.UserModelAssembler;
import com.benhession.spoonfull_rest_service.security.UserDetailsForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping(value = "register", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody UserDetailsForm form) {
        return userService.save(form.toUser(passwordEncoder));
    }

    @GetMapping(value = "/csrf-token")
    public @ResponseBody String getCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        response.addHeader("_csrf", token.getToken());
        response.addHeader("_csrf_header", token.getHeaderName());
        return "";
    }

    @GetMapping(value = "/current_user")
    public ResponseEntity<UserModel> getCurrentUser(@AuthenticationPrincipal User user) {

        Optional<User> currentUser = Optional.ofNullable(user);

        return currentUser.map(cU -> new ResponseEntity<>(new UserModelAssembler().toModel(cU), HttpStatus.OK))
                    .orElseGet(() ->new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
