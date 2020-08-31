package com.benhession.spoonfull_rest_service.representation_models;

import com.benhession.spoonfull_rest_service.model.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Relation(value = "recipe", collectionRelation = "recipes")
public class RecipeModel extends RepresentationModel<RecipeModel> {

    @Getter
    private final String name;

    @Getter
    private final Duration prepTime;

    @Getter
    private final Duration cookTime;

    @Getter
    private final Duration totalTime;

    @Getter
    private final String yield;

    @Getter
    private final String imgUrl;

    @Getter
    private final String sourceUrl;

    @Getter
    private final List<String> givenCategories;

    @Getter
    private final List<String> ingredients;

    @Getter
    private final List<String> method;

    @Getter
    private final List<String> keywords;

    public RecipeModel(Recipe recipe) {
        this.name = recipe.getName();
        this.prepTime = recipe.getPrepTime();
        this.cookTime = recipe.getCookTime();
        this.totalTime = recipe.getTotalTime();
        this.yield = recipe.getYield();
        this.imgUrl = recipe.getImgUrl();
        this.sourceUrl = recipe.getSourceUrl();
        this.givenCategories = recipe.getGivenCategories().stream().map(GivenCategory::getCategory).collect(Collectors.toList());
        this.ingredients = recipe.getIngredients().stream().map(Ingredient::getDescription).collect(Collectors.toList());
        this.method = recipe.getMethod().stream().map(Method::getStep).collect(Collectors.toList());
        this.keywords = recipe.getKeywords().stream().map(Keyword::getValue).collect(Collectors.toList());
    }
}
