package com.benhession.spoonfull_rest_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"givenCategories", "ingredients", "keywords", "method"})
@Table(name = "recipe")

public class Recipe {

    @Id
    @GeneratedValue
    @Column(name = "recipe_id")
    private int recipeId;

    @Column(name = "name")
    private String name;

    @Column(name = "prep_time")
    private Duration prepTime;

    @Column(name = "cook_time")
    private Duration cookTime;

    @Column(name = "total_time")
    private Duration totalTime;

    @Column(name = "yield")
    private String yield;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "hash_value")
    private String hashValue;

    @OneToMany(targetEntity = GivenCategory.class, mappedBy = "recipe")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<GivenCategory> givenCategories;

    @OneToMany(targetEntity = Ingredient.class, mappedBy = "recipe")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Ingredient> ingredients;

    @OneToMany(targetEntity = Method.class, mappedBy = "recipe")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Method> method;

    @OneToMany(targetEntity = Keyword.class, mappedBy = "recipe")
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Keyword> keywords;

}
