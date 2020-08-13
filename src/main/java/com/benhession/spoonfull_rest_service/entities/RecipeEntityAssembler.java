package com.benhession.spoonfull_rest_service.entities;

import com.benhession.spoonfull_rest_service.controllers.RecipeController;
import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class RecipeEntityAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeEntity> {

    public RecipeEntityAssembler() {
        super(RecipeController.class, RecipeEntity.class);
    }

    @Override
    protected RecipeEntity instantiateModel(Recipe recipe) {
        return new RecipeEntity(recipe);
    }

    @Override
    public RecipeEntity toModel(Recipe recipe) {
        return createModelWithId(recipe.getRecipeId(), recipe);
    }


}
