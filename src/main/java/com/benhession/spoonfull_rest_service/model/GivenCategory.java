package com.benhession.spoonfull_rest_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "given_categories")
public class GivenCategory {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NonNull
    @Column(length = 500)
    public String category;

    @Override
    public String toString() {
        return "GivenCategory{" +
                "category='" + category + '\'' +
                '}';
    }
}