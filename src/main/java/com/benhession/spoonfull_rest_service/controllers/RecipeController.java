package com.benhession.spoonfull_rest_service.controllers;

import com.benhession.spoonfull_rest_service.data.RecipeService;
import com.benhession.spoonfull_rest_service.entities.RecipeEntity;
import com.benhession.spoonfull_rest_service.entities.RecipeEntityAssembler;
import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/recipes", produces = "application/json")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeEntity> recipeById(@PathVariable("id") int id) {
        Optional<Recipe> recipe = recipeService.recipeFromId(id);

        return recipe.map(
                theRecipe -> new ResponseEntity<>(new RecipeEntityAssembler().toModel(theRecipe), HttpStatus.OK)
        ).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping("find_by")
    public ResponseEntity<CollectionModel<RecipeEntity>> recipesBy(@RequestParam Optional<String> name,
                                                                   @RequestParam Optional<List<String>> ingredients,
                                                                   @RequestParam Optional<List<String>> keywords,
                                                                   @RequestParam Optional<List<String>> categories,
                                                                   @RequestParam int page, @RequestParam int size) {

        boolean pageInRange;
        Page<Recipe> recipes;
        Set<Integer> ids = new HashSet<>();
        CollectionModel<RecipeEntity> recipeEntities;

        name.ifPresent(theName -> ids.addAll(recipeService.findRecipeIdFromName(theName)));

        ingredients.ifPresent(i -> addToOrRetainIds(ids, i, recipeService::findRecipeIdsFromIngredientIn));

        keywords.ifPresent(k -> addToOrRetainIds(ids, k, recipeService::findRecipeIdsFromKeywordsIn));

        categories.ifPresent(c -> addToOrRetainIds(ids, c, recipeService::findRecipesFromGivenCategoriesIn));

        if (!ids.isEmpty()) {
            PageRequest pageRequest = PageRequest.of(page, size);
            recipes = recipeService.findRecipesFromIdIn(ids, pageRequest);
            pageInRange = page < recipes.getTotalPages();

            recipeEntities = new RecipeEntityAssembler().toCollectionModel(recipes);

        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        recipeEntities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class)
                                .recipesBy(name, ingredients, keywords, categories, page, size)
                ).withRel("self").expand()
        );

        recipeEntities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class)
                                .recipesBy(name, ingredients, keywords, categories, 0, size)
                ).withRel("first_page").expand()
        );

        if (!recipes.isFirst()) {
            recipeEntities.add(
                    WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(RecipeController.class)
                                    .recipesBy(name, ingredients, keywords, categories,
                                            recipes.previousOrFirstPageable().getPageNumber(), size)
                    ).withRel("previous_page").expand()
            );
        }

        if (!recipes.isLast()) {
            recipeEntities.add(
                    WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(RecipeController.class)
                                    .recipesBy(name, ingredients, keywords, categories,
                                            recipes.nextOrLastPageable().getPageNumber(), size)
                    ).withRel("next_page").expand()
            );
        }

        recipeEntities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class)
                                .recipesBy(name, ingredients, keywords, categories,
                                        recipes.getTotalPages() - 1, size)
                ).withRel("last_page").expand()
        );


        return pageInRange
                ? new ResponseEntity<>(recipeEntities, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    /**
     * Single method interface for a find ids query
     */
    private interface IdQuery {
        Collection<Integer> findIds(List<String> searchWords);
    }

    /**
     * Add all ids from a query to the set if it is empty, otherwise retain the items that are present
     * in the set already
     *
     * @param ids       the id set
     * @param paramList the the list of values from a request parameter
     * @param idQuery   the method reference or lambda expression that finds the ids
     */
    private void addToOrRetainIds(Set<Integer> ids, List<String> paramList, IdQuery idQuery) {

        if (ids.isEmpty()) {
            ids.addAll(idQuery.findIds(paramList));
        } else {
            ids.retainAll(idQuery.findIds(paramList));
        }
    }
}