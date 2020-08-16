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

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping
    public ResponseEntity<CollectionModel<RecipeEntity>> findRecipes(@RequestParam Optional<Integer> page,
                                                            @RequestParam Optional<Integer> size) {

        int numOfRecipes = recipeService.count();
        Iterable<Recipe> recipes;
        ResponseEntity<CollectionModel<RecipeEntity>> response;

        Optional<PageRequest> pageRequest = (page.isPresent() && size.isPresent())
                ? Optional.of(PageRequest.of(page.get(), size.get()))
                : Optional.empty();

        recipes = pageRequest.isPresent()
                ? recipeService.recipesFrom(pageRequest.get())
                : recipeService.findAll();

        CollectionModel<RecipeEntity> entities = new RecipeEntityAssembler().toCollectionModel(recipes);

        response = pageRequest.map(
                request ->
                        pageableRecipeResponse(entities, request, numOfRecipes)
        )
                .orElseGet(() -> {

                    entities.add(
                            WebMvcLinkBuilder.linkTo(
                                    WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(page, size)
                            ).withRel("self")
                    );

                    return new ResponseEntity<>(entities, HttpStatus.OK);
                });

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeEntity> recipeById(@PathVariable("id") int id) {
        Optional<Recipe> recipe = recipeService.recipeFromId(id);

        return recipe.map(
                theRecipe -> new ResponseEntity<>(new RecipeEntityAssembler().toModel(theRecipe), HttpStatus.OK)
        ).orElseGet(
                () -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
        );
    }

    @GetMapping("find-by-ingredients")
    public ResponseEntity<CollectionModel<RecipeEntity>> recipeByIngredients(@RequestParam List<String> ingredients,
                                                            @RequestParam int page, @RequestParam int size) {

        List<Integer> recipeIds = recipeService.findRecipeIdsFromIngredientIn(ingredients);

        return produceResponseFromIds(recipeIds, page, size);


    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @GetMapping("find_by")
    public ResponseEntity<CollectionModel<RecipeEntity>> recipesBy(@RequestParam Optional<String> name,
                                                                   @RequestParam Optional<List<String>> ingredients,
                                                                   @RequestParam Optional<List<String>> keywords,
                                                                   @RequestParam Optional<List<String>> categories,
                                                                   @RequestParam int page, @RequestParam int size) {

        Set<Integer> ids = new HashSet<>();

        name.ifPresent(theName -> ids.addAll(recipeService.findRecipeIdFromName(theName)));

        ingredients.ifPresent(i -> addToOrRetainIds(ids, i, recipeService::findRecipeIdsFromIngredientIn));

        keywords.ifPresent(k -> addToOrRetainIds(ids, k, recipeService::findRecipeIdsFromKeywordsIn));

        categories.ifPresent(c -> addToOrRetainIds(ids, c, recipeService::findRecipesFromGivenCategoriesIn));

        return produceResponseFromIds(ids, page, size);

    }

    private interface CanFindIds {
        Collection<Integer> findIds(List<String> searchWords);
    }

    private void addToOrRetainIds(Set<Integer> ids, List<String> param, CanFindIds lambda) {

        if(ids.isEmpty()) {
            ids.addAll(lambda.findIds(param));
        } else {
            ids.retainAll(lambda.findIds(param));
        }
    }

    private ResponseEntity<CollectionModel<RecipeEntity>> produceResponseFromIds(Collection<Integer> ids, int page, int size) {

        Optional<ResponseEntity<CollectionModel<RecipeEntity>>> response = Optional.empty();

        if(!ids.isEmpty()) {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<Recipe> recipes = recipeService.findRecipesFromIdIn(ids, pageRequest);

            CollectionModel<RecipeEntity> entities = new RecipeEntityAssembler().toCollectionModel(recipes);
            response = Optional.of(pageableRecipeResponse(entities, pageRequest, ids.size()));
        }

        return response.orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
    }

    private ResponseEntity<CollectionModel<RecipeEntity>> pageableRecipeResponse(CollectionModel<RecipeEntity> entities,
                                                                PageRequest request, int totalNumOfEntities) {

        boolean pageInRange = true;

        int firstPage = request.first().getPageNumber();
        int lastPage =  totalNumOfEntities / request.getPageSize();
        int currentPage = request.getPageNumber();
        int pageSize = request.getPageSize();

        if (totalNumOfEntities % pageSize == 0) {
            lastPage -= 1;
        }

        if (currentPage > lastPage) {
            pageInRange = false;
        }

        entities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(
                                Optional.of(currentPage),
                                Optional.of(pageSize)
                        )
                ).withRel("self")
        );

        entities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(
                                Optional.of(firstPage),
                                Optional.of(pageSize)
                        )
                ).withRel("firstPage")

        );

        if (currentPage != firstPage) {
            entities.add(
                    WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(
                                    Optional.of(request.previousOrFirst().getPageNumber()),
                                    Optional.of(pageSize)
                            )
                    ).withRel("previousPage")
            );
        }

        if (currentPage != lastPage) {
            entities.add(
                    WebMvcLinkBuilder.linkTo(
                            WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(
                                    Optional.of(request.next().getPageNumber()),
                                    Optional.of(pageSize)
                            )
                    ).withRel("nextPage")
            );
        }

        entities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(
                                Optional.of(lastPage),
                                Optional.of(pageSize)
                        )
                ).withRel("lastPage")
        );


        return pageInRange
                ? new ResponseEntity<>(entities, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);


    }

}
