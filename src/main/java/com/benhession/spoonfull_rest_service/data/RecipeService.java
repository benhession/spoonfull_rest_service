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

    public Long count() {
        return recipeRepository.count();
    }

    public Iterable<Recipe> findByIngredients(List<String> ingredients, Pageable pageable) {

        List<Integer> ids = findIngredientIds(ingredients);

        return recipeRepository.findAllByRecipeIDIn(ids, pageable);
    }

    public Long countRecipesByIngredients(List<String> ingredients) {

        return (long) findIngredientIds(ingredients).size();
    }

    private List<Integer> findIngredientIds(List<String> ingredients) {

        List<String> theIngredients = new ArrayList<>(ingredients);

        Set<Integer> ids = new HashSet<>(recipeRepository.recipeIdsFromIngredient(theIngredients.remove(0)));

        theIngredients.forEach(ingredient -> ids.retainAll(recipeRepository.recipeIdsFromIngredient(ingredient)));

        return new ArrayList<>(ids);
    }
}
