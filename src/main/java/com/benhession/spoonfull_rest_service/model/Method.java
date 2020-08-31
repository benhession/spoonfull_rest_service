package com.benhession.spoonfull_rest_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "methods")
public class Method {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NonNull
    @Column(length = 5000)
    public String step;

    @Override
    public String toString() {
        return "Method{" +
                "step='" + step + '\'' +
                '}';
    }
}
