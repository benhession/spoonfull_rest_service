package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
}
