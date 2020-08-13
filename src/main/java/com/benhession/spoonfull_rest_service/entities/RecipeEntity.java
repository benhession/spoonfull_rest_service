package com.benhession.spoonfull_rest_service.entities;

import com.benhession.spoonfull_rest_service.model.Ingredient;
import com.benhession.spoonfull_rest_service.model.Recipe;
import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Relation(value = "recipe", collectionRelation = "recipes")
public class RecipeEntity extends RepresentationModel<RecipeEntity> {

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

    public RecipeEntity(Recipe recipe) {
        this.name = recipe.getName();
        this.prepTime = recipe.getPrepTime();
        this.cookTime = recipe.getCookTime();
        this.totalTime = recipe.getTotalTime();
        this.yield = recipe.getYield();
        this.imgUrl = recipe.getImgUrl();
        this.sourceUrl = recipe.getSourceUrl();
        this.givenCategories = recipe.getGivenCategories();
        this.ingredients = recipe.getIngredients().stream().map(Ingredient::getDescription).collect(Collectors.toList());
        this.method = recipe.getMethod();
        this.keywords = recipe.getKeywords();
    }
}
