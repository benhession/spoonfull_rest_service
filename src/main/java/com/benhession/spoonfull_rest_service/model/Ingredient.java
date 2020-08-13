package com.benhession.spoonfull_rest_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(length = 1000)
    @NonNull
    public String description;

    @Override
    public String toString() {
        return "Ingredient{" +
                "description='" + description + '\'' +
                '}';
    }
}
