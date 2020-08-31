package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserFavouriteRepository extends PagingAndSortingRepository<UserFavourite, Integer> {

    UserFavourite findByIdAndUser(int id, User user);

}
