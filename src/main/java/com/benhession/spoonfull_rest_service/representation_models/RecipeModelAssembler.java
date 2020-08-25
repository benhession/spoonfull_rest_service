package com.benhession.spoonfull_rest_service.representation_models;

import com.benhession.spoonfull_rest_service.controllers.RecipeController;
import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class RecipeModelAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeModel> {

    public RecipeModelAssembler() {
        super(RecipeController.class, RecipeModel.class);
    }

    @Override
    protected RecipeModel instantiateModel(Recipe recipe) {
        return new RecipeModel(recipe);
    }

    @Override
    public RecipeModel toModel(Recipe recipe) {
        return createModelWithId(recipe.getRecipeId(), recipe);
    }


}
