package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeService {

    RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Iterable<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> recipesFrom(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    public Optional<Recipe> recipeFromId(int id) {
        return recipeRepository.findById(id);
    }

    public int count() {
        return Math.toIntExact(recipeRepository.count());
    }

    public Page<Recipe> findRecipesFromIdIn(List<Integer> ids, Pageable pageable) {
        return recipeRepository.findAllByRecipeIdIn(ids, pageable);
    }

    public List<Integer> findRecipeIdsFromIngredientIn(List<String> ingredients) {

        List<String> theIngredients = new ArrayList<>(ingredients);

        Set<Integer> ids = new HashSet<>(recipeRepository.recipeIdsFromIngredient(theIngredients.remove(0)));

        theIngredients.forEach(ingredient -> ids.retainAll(recipeRepository.recipeIdsFromIngredient(ingredient)));

        return new ArrayList<>(ids);
    }



}
