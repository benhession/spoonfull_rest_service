package com.benhession.spoonfull_rest_service.data;

import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface RecipeRepository extends PagingAndSortingRepository<Recipe, Integer> {
}
