package com.benhession.spoonfull_rest_service.representation_models;

import com.benhession.spoonfull_rest_service.controllers.UserFavouritesController;
import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserFavouritesController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User user) {

        UserModel userModel = instantiateModel(user);

        userModel.setUsername(user.getUsername());
        userModel.setFavourites(toFavouritesModel(user.getFavourites()));

        return userModel;
    }

    private List<UserFavouriteModel> toFavouritesModel(Set<UserFavourite> favourites) {
        if (favourites.isEmpty())
            return Collections.emptyList();

        return favourites.stream()
                .map(favourite -> UserFavouriteModel.builder()
                            .recipeName(favourite.getRecipe().getName())
                            .category(favourite.getUserCategory())
                            .recipe(new RecipeModelAssembler().toModel(favourite.getRecipe()))
                            .build()
                            .add(WebMvcLinkBuilder.linkTo(
                                    WebMvcLinkBuilder.methodOn(UserFavouritesController.class)
                                    .userFavouriteById(favourite.getId(), favourite.getUser())
                                ).withSelfRel()
                            )

                ).collect(Collectors.toList());
    }

}
