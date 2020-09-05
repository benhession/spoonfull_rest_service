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

    public Optional<Recipe> recipeFromId(long id) {
        return recipeRepository.findById(id);
    }

    public Page<Recipe> findRecipesFromIdIn(Collection<Long> ids, Pageable pageable) {

        List<Long> idList = new LinkedList<>(ids);

        return recipeRepository.findAllByRecipeIdIn(idList, pageable);
    }

    public List<Long> findRecipeIdFromName(String name) {
        return recipeRepository.recipeIdsFromRecipeName(name);
    }

    public List<Long> findRecipeIdsFromIngredientIn(List<String> ingredients) {

        List<String> theIngredients = new LinkedList<>(ingredients);

        Set<Long> ids = new HashSet<>(recipeRepository.recipeIdsFromIngredient(theIngredients.remove(0)));

        theIngredients.forEach(ingredient -> ids.retainAll(recipeRepository.recipeIdsFromIngredient(ingredient)));

        return new ArrayList<>(ids);
    }

    public List<Long> findRecipeIdsFromKeywordsIn(List<String> keywords) {

        List<String> theKeywords = new LinkedList<>(keywords);

        Set<Long> ids = new HashSet<>(recipeRepository.recipeIdsFromKeyword(theKeywords.remove(0)));

        theKeywords.forEach(keyword -> ids.retainAll(recipeRepository.recipeIdsFromKeyword(keyword)));

        return new ArrayList<>(ids);
    }

    public List<Long> findRecipesFromGivenCategoriesIn(List<String> categories) {

        List<String> theCategories = new LinkedList<>(categories);

        Set<Long> ids = new HashSet<>(recipeRepository.recipeIdsFromGivenCategory(theCategories.remove(0)));

        theCategories.forEach(category -> ids.retainAll(recipeRepository.recipeIdsFromGivenCategory(category)));

        return new ArrayList<>(ids);
    }


}
