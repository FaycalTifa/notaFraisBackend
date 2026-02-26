package com.example.notaFraisBackend.entities.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Collaborateur collaborateur;

    public CustomUserDetails(Collaborateur collaborateur) {
        this.collaborateur = collaborateur;
    }

    public Long getId() {
        return collaborateur.getId();
    }

    public Collaborateur getCollaborateur() {
        return collaborateur;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + collaborateur.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return collaborateur.getPassword();
    }

    @Override
    public String getUsername() {
        return collaborateur.getEmail();
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
        return collaborateur.isActif();
    }
}
