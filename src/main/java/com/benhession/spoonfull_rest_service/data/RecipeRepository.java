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
interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

    Page<Recipe> findAllByRecipeIdIn(List<Long> ids, Pageable pageable);

    @Query(value =  "SELECT r.recipeId " +
                    "FROM Recipe r " +
                    "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Long> recipeIdsFromRecipeName(@Param("name") String recipeName);

    @Query(value =  "SELECT i.recipe.recipeId " +
                    "FROM Ingredient i "+
                    "WHERE LOWER(i.description) LIKE LOWER(CONCAT('%', :ingredient, '%')) ")
    List<Long> recipeIdsFromIngredient(@Param("ingredient") String ingredient);

    @Query(value =  "SELECT k.recipe.recipeId " +
                    "FROM Keyword k " +
                    "WHERE LOWER(k.value) LIKE LOWER(CONCAT('%', :keyword, '%')) ")
    List<Long> recipeIdsFromKeyword(@Param("keyword") String keyword);

    @Query(value =  "SELECT c.recipe.recipeId " +
                    "FROM GivenCategory c " +
                    "WHERE LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')) ")
    List<Long> recipeIdsFromGivenCategory(@Param("category") String category);
}
