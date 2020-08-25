package com.benhession.spoonfull_rest_service.controllers;

import com.benhession.spoonfull_rest_service.data.RecipeService;
import com.benhession.spoonfull_rest_service.data.UserService;
import com.benhession.spoonfull_rest_service.model.Recipe;
import com.benhession.spoonfull_rest_service.model.User;
import com.benhession.spoonfull_rest_service.model.UserFavourite;
import com.benhession.spoonfull_rest_service.representation_models.UserFavouriteModel;
import com.benhession.spoonfull_rest_service.representation_models.UserFavouriteModelAssembler;
import com.benhession.spoonfull_rest_service.representation_models.UserModel;
import com.benhession.spoonfull_rest_service.representation_models.UserModelAssembler;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

        Optional<UserFavourite> userFavourite = Optional.ofNullable(userService.UserFavouriteById(id, user));

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
     * @return a response of type userModel for the authenticated user
     */
    @PostMapping("/add_favourite")
    public ResponseEntity<UserModel> addFavourite(AddFavouriteForm form, @AuthenticationPrincipal User user) {

        Optional<Recipe> theRecipe = recipeService.recipeFromId(form.getRecipe_id());
        Optional<UserModel> createdUser = Optional.empty();
        Optional<User> theUser = userService.findUserById(user.getId());

        // check if the recipe id is already present in the users favourites
        if(theUser.isPresent() &&
                theUser.get().getFavourites().stream()
                        .anyMatch((f -> f.getRecipe().getRecipeId() == form.getRecipe_id()))){

            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        // if the recipe id is a valid recipe add it to the users favourites
        if (theRecipe.isPresent()
                && user.addFavourite(new UserFavourite(form.getCategory(), theRecipe.get()))) {

            Optional<User> returnedUser = Optional.ofNullable(userService.save(user));
            createdUser = returnedUser.map(u -> new UserModelAssembler().toModel(u));

        }

        return createdUser.map(u -> new ResponseEntity<>(u, HttpStatus.CREATED))
                    .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED));

    }

    @Data
    private static class AddFavouriteForm {
        int recipe_id;
        String category;
    }
}
