package com.benhession.spoonfull_rest_service.controllers;

import com.benhession.spoonfull_rest_service.data.RecipeService;
import com.benhession.spoonfull_rest_service.data.UserService;
import com.benhession.spoonfull_rest_service.model.Recipe;
import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import com.benhession.spoonfull_rest_service.representation_models.UserFavouriteModel;
import com.benhession.spoonfull_rest_service.representation_models.UserFavouriteModelAssembler;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user_favourites")
@CrossOrigin("*")
public class UserFavouritesController {
    private final UserService userService;
    private final RecipeService recipeService;

    public UserFavouritesController(UserService userService, RecipeService recipeService) {
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFavouriteModel> userFavouriteById(@PathVariable("id") int id,
                                                                @AuthenticationPrincipal User user) {


        Optional<UserFavourite> userFavourite = Optional.ofNullable(userService.userFavouriteById(id, user));

        if (userFavourite.isPresent()) {
            UserFavouriteModel model = new UserFavouriteModelAssembler().toModel(userFavourite.get());

            return new ResponseEntity<>(model, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    /**
     * Adds a new favourite for the authenticated user if the recipe id is not already a favourite. Otherwise returns
     * a HTTP status of CONFLICT if the recipe is already a favourite or a HTTP status of EXPECTATION_FAILED if the
     * recipe could not be added as a favourite.
     * @param form the body of the post request of type AddFavouriteForm
     * @param user the authenticated user
     * @return Response of CREATED containing the favourites of the authenticated user
     */
    @PostMapping("/")
    @Transactional
    public ResponseEntity<CollectionModel<UserFavouriteModel>> addFavourite(AddFavouriteForm form,
                                                                            @AuthenticationPrincipal User user) {

        Optional<Recipe> theRecipe = recipeService.recipeFromId(form.getRecipe_id());
        Optional<CollectionModel<UserFavouriteModel>> favouriteModels = Optional.empty();
        User theUser = userService.findUserById(user.getId())
                            .orElseThrow(() -> new HttpServerErrorException(HttpStatus.EXPECTATION_FAILED,
                                    "Unable to get user with favourites from database"));


        // check if the recipe id is already present in the users favourites
        if(theUser.getFavourites().stream()
                        .anyMatch((f -> f.getRecipe().getRecipeId() == form.getRecipe_id()))){

            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        }

        // if the recipe id is a valid recipe add it to the users favourites
        if (theRecipe.isPresent()
                && theUser.addFavourite(new UserFavourite(form.getCategory(), theRecipe.get()))) {

            Optional<User> returnedUser = Optional.ofNullable(userService.save(theUser));

            favouriteModels = returnedUser.map(u -> new UserFavouriteModelAssembler()
                    .toCollectionModel(u.getFavourites()));

        }

        return favouriteModels.map(models -> new ResponseEntity<>(models, HttpStatus.CREATED))
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED));

    }

    @Data
    private static class AddFavouriteForm {
        int recipe_id;
        String category;
    }

    @GetMapping("/")
    public ResponseEntity<CollectionModel<UserFavouriteModel>> getUserFavourites(@AuthenticationPrincipal User user) {

        User theUser = userService.findUserById(user.getId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.EXPECTATION_FAILED,
                        "Unable to get user with favourites from database"));

        Set<UserFavourite> userFavourites = theUser.getFavourites();

        if(!userFavourites.isEmpty()) {

            CollectionModel<UserFavouriteModel> favCollection =
                    new UserFavouriteModelAssembler().toCollectionModel(userFavourites);

            return new ResponseEntity<>(favCollection, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<UserFavouriteModel> deleteUserFavourite(@PathVariable(name = "id") int favouriteId,
                                                                  @AuthenticationPrincipal User user) {

        User theUser = userService.findUserById(user.getId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.EXPECTATION_FAILED,
                "Unable to get user with favourites from database"));

        UserFavourite theFavourite = Optional.of(userService.userFavouriteById(favouriteId, user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to find favourite"));

        Set<UserFavourite> favourites = theUser.getFavourites();

        if(favourites.remove(theFavourite)) {

            userService.removeFavourite(theFavourite, theUser);
            UserFavouriteModel model = new UserFavouriteModelAssembler().toModel(theFavourite);
            return new ResponseEntity<>(model, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<UserFavouriteModel> changeCategory(@PathVariable(name = "id") int favouriteId,
                                                             @RequestBody NewCategoryForm form,
                                                             @AuthenticationPrincipal User user) {

        User theUser = userService.findUserById(user.getId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.EXPECTATION_FAILED,
                        "Unable to get user with favourites from database"));

        UserFavourite theFavourite = Optional.ofNullable(userService.userFavouriteById(favouriteId, user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Unable to find the favourite"));

        String newCategory = Optional.ofNullable(form.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Category not present"));

        theFavourite.setUserCategory(newCategory);
        UserFavouriteModel favouriteModel = new UserFavouriteModelAssembler().toModel(theFavourite);

        Optional<User> returnedUser = Optional.ofNullable(userService.save(theUser));

        return returnedUser.map(u -> new ResponseEntity<>(favouriteModel, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_MODIFIED));

    }

    @Data
    private static class NewCategoryForm {
        String category;
    }
}
