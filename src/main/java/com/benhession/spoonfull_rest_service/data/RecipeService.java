package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
