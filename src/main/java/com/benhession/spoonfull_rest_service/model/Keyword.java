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
@Table(name = "keywords")
public class Keyword {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NonNull
    public String value;

    @Override
    public String toString() {
        return "Keyword{" +
                "value='" + value + '\'' +
                '}';
    }
}
