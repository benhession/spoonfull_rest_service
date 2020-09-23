package com.benhession.spoonfull_rest_service.security;

import com.benhession.spoonfull_rest_service.data.UserService;
import com.benhession.spoonfull_rest_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPrincipalExtractor {

    UserService userService;

    @Autowired
    public UserPrincipalExtractor(UserService userService) {
        this.userService = userService;
    }

    public User extractUser(Jwt jwt) {

        String subjectId = jwt.getSubject();
        String username = jwt.getClaim("user_name");

        Optional<User> theUser = userService.findUserByAuthId(subjectId);

        if(theUser.isEmpty()) {
            theUser = Optional.of(userService.save(new User(username, subjectId)));
        }

        return theUser.orElseThrow(() -> new SecurityException("Unable to get principal"));
    }
}
