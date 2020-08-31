package com.benhession.spoonfull_rest_service.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

@Data
@Entity
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
    private List<GivenCategory> givenCategories;

    @OneToMany(targetEntity = Ingredient.class, mappedBy = "recipe")
    private List<Ingredient> ingredients;

    @ElementCollection
    @CollectionTable(name = "method")
    @Column(length = 5000, name = "step")
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id")
    private List<String> method;

    @OneToMany(targetEntity = Keyword.class, mappedBy = "recipe")
    private List<Keyword> keywords;

}
