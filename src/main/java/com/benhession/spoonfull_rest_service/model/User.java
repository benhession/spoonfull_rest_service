package com.benhession.spoonfull_rest_service.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@EqualsAndHashCode(exclude = "favourites")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    @OneToMany(targetEntity = UserFavourite.class, mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserFavourite> favourites;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favourites = new HashSet<>();
    }

    public boolean addFavourite(UserFavourite userFavourite) {
        userFavourite.setUser(this);
        return favourites.add(userFavourite);
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
