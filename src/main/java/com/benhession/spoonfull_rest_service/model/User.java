package com.benhession.spoonfull_rest_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@EqualsAndHashCode(exclude = "favourites")
@NamedEntityGraph(name = "User.favourites", attributeNodes = @NamedAttributeNode("favourites"))
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String authId;

    @OneToMany(targetEntity = UserFavourite.class, mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFavourite> favourites;

    public User() {}

    public User(String username, String authId) {
        this.username = username;
        this.authId = authId;
        this.favourites = new HashSet<>();
    }

    public boolean addFavourite(UserFavourite userFavourite) {
        userFavourite.setUser(this);
        return favourites.add(userFavourite);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
