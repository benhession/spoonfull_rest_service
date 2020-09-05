package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return userRepository.save(user);
    }

    public UserFavourite userFavouriteById(long id, User user) {
        return userFavouriteRepository.findByIdAndUser(id, user);
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(userRepository.findUserById(id));
    }

    public void removeFavourite(UserFavourite favourite, User user) {
        userFavouriteRepository.delete(favourite);
        userRepository.save(user);
    }
}
