package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer> {

    Page<Recipe> findAllByRecipeIdIn(List<Integer> ids, Pageable pageable);

    @Query(value = "SELECT i.recipe.recipeId " +
            "FROM Ingredient i " +
            "WHERE LOWER(i.description) LIKE LOWER(CONCAT('%',:ingredient, '%')) ")
    List<Integer> recipeIdsFromIngredient(@Param("ingredient") String ingredient);
}
