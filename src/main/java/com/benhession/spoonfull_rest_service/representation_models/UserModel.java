package com.benhession.spoonfull_rest_service.representation_models;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(value = "user", collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {

    private String username;
    private List<UserFavouriteModel> favourites;

}
