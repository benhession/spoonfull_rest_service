package com.benhession.spoonfull_rest_service.representation_models;

import com.benhession.spoonfull_rest_service.controllers.UserFavouritesController;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class UserFavouriteModelAssembler extends RepresentationModelAssemblerSupport<UserFavourite,
        UserFavouriteModel> {

    public UserFavouriteModelAssembler() {
        super(UserFavouritesController.class, UserFavouriteModel.class);
    }

    @Override
    public UserFavouriteModel toModel(UserFavourite favourite) {

        UserFavouriteModel favouriteModel = instantiateModel(favourite);

        favouriteModel.setCategory(favourite.getUserCategory());
        favouriteModel.setRecipeName(favourite.getRecipe().getName());
        favouriteModel.setId(favourite.getId());
        favouriteModel.setRecipeModel(new RecipeModelAssembler().toModel(favourite.getRecipe()));

        favouriteModel.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserFavouritesController.class)
                .userFavouriteById(favourite.getId(), favourite.getUser())
            ).withSelfRel()
        );

        return favouriteModel;
    }

}
