package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserFavouriteRepository userFavouriteRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserFavouriteRepository userFavouriteRepository) {
        this.userRepository = userRepository;
        this.userFavouriteRepository = userFavouriteRepository;
    }

    public User save(User user) {
        userRepository.save(user);

        return user;
    }

    public UserFavourite UserFavouriteById(int id, User user) {
        return userFavouriteRepository.findByIdAndUser(id, user);
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(userRepository.findUserById(id));
    }

}
