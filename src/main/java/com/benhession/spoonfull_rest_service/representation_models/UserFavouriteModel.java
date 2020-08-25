package com.benhession.spoonfull_rest_service.representation_models;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Relation(value = "user_favourite", collectionRelation = "user_favourites")
public class UserFavouriteModel extends RepresentationModel<UserFavouriteModel> {

    private int id;
    private String recipeName;
    private String category;
    private RecipeModel recipeModel;

}
