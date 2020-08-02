package com.benhession.spoonfull_rest_service.controllers;

import com.benhession.spoonfull_rest_service.data.RecipeService;
import com.benhession.spoonfull_rest_service.entities.RecipeEntity;
import com.benhession.spoonfull_rest_service.entities.RecipeEntityAssembler;
import com.benhession.spoonfull_rest_service.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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

        final int recipeCount = recipeService.count().intValue();
        AtomicBoolean pageInRange = new AtomicBoolean(true);
        Iterable<Recipe> recipes;


        Optional<PageRequest> pageRequest = (page.isPresent() && size.isPresent())
                ? Optional.of(PageRequest.of(page.get(), size.get()))
                : Optional.empty();

        recipes = pageRequest.isPresent()
                ? recipeService.recipesFrom(pageRequest.get())
                : recipeService.findAll();

        CollectionModel<RecipeEntity> entities = new RecipeEntityAssembler().toCollectionModel(recipes);

        entities.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(RecipeController.class).findRecipes(page, size)
                ).withRel("self")
        );

        pageRequest.ifPresent(
                request -> {
                    int firstPage = request.first().getPageNumber();
                    int lastPage = recipeCount / request.getPageSize();
                    int currentPage = request.getPageNumber();
                    int pageSize = request.getPageSize();

                    if (recipeCount % pageSize == 0) {
                        lastPage -= 1;
                    }

                    if (currentPage > lastPage) {
                        pageInRange.set(false);
                    }

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
                }
        );

        return pageInRange.get()
                ? new ResponseEntity<>(entities, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
}
