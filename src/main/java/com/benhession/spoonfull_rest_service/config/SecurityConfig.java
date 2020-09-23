package com.benhession.spoonfull_rest_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests(auth -> auth
                    .antMatchers("/recipes", "/recipes/**",
                            "/user_favourites", "user_favourites/**", "user/current_user").hasAuthority("SCOPE_USER")
                    .antMatchers("/", "/**").permitAll()
                    .anyRequest().authenticated()
                )

                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        ;
    }

}
