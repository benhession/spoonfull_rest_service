package com.benhession.spoonfull_rest_service.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Table(name = "user_favourite")
public class UserFavourite {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private User user;

    @NonNull
    private String userCategory;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    @NonNull
    private Recipe recipe;

    @Override
    public String toString() {
        return "UserFavourite{" +
                "id=" + id +
                "user=" + user.getUsername() +
                ", userCategory='" + userCategory + '\'' +
                '}';
    }
}
