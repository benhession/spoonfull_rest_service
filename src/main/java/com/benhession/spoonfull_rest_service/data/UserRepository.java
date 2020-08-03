package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
