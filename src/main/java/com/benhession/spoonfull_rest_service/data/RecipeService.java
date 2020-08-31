package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecipeService {

    RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Optional<Recipe> recipeFromId(int id) {
        return recipeRepository.findById(id);
    }

    public Page<Recipe> findRecipesFromIdIn(Collection<Integer> ids, Pageable pageable) {

        List<Integer> idList = new LinkedList<>(ids);

        return recipeRepository.findAllByRecipeIdIn(idList, pageable);
    }

    public List<Integer> findRecipeIdFromName(String name) {
        return recipeRepository.recipeIdsFromRecipeName(name);
    }

    public List<Integer> findRecipeIdsFromIngredientIn(List<String> ingredients) {

        List<String> theIngredients = new LinkedList<>(ingredients);

        Set<Integer> ids = new HashSet<>(recipeRepository.recipeIdsFromIngredient(theIngredients.remove(0)));

        theIngredients.forEach(ingredient -> ids.retainAll(recipeRepository.recipeIdsFromIngredient(ingredient)));

        return new ArrayList<>(ids);
    }

    public List<Integer> findRecipeIdsFromKeywordsIn(List<String> keywords) {

        List<String> theKeywords = new LinkedList<>(keywords);

        Set<Integer> ids = new HashSet<>(recipeRepository.recipeIdsFromKeyword(theKeywords.remove(0)));

        theKeywords.forEach(keyword -> ids.retainAll(recipeRepository.recipeIdsFromKeyword(keyword)));

        return new ArrayList<>(ids);
    }

    public List<Integer> findRecipesFromGivenCategoriesIn(List<String> categories) {

        List<String> theCategories = new LinkedList<>(categories);

        Set<Integer> ids = new HashSet<>(recipeRepository.recipeIdsFromGivenCategory(theCategories.remove(0)));

        theCategories.forEach(category -> ids.retainAll(recipeRepository.recipeIdsFromGivenCategory(category)));

        return new ArrayList<>(ids);
    }


}
