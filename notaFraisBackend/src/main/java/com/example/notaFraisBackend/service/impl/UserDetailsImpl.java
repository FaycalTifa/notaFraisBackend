package com.example.notaFraisBackend.service.impl;

import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {


    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String nomComplet;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private Long directionId;
    private Long serviceId;
    private Long sectionId;

    public UserDetailsImpl(Long id, String email, String nomComplet, String password,
                           Collection<? extends GrantedAuthority> authorities,
                           Long directionId, Long serviceId, Long sectionId) {
        this.id = id;
        this.email = email;
        this.nomComplet = nomComplet;
        this.password = password;
        this.authorities = authorities;
        this.directionId = directionId;
        this.serviceId = serviceId;
        this.sectionId = sectionId;
    }

    public static UserDetailsImpl build(Collaborateur collaborateur) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + collaborateur.getRole().name());

        return new UserDetailsImpl(
                collaborateur.getId(),
                collaborateur.getEmail(),
                collaborateur.getNomComplet(),
                collaborateur.getPassword(),
                Collections.singletonList(authority),
                collaborateur.getDirection() != null ? collaborateur.getDirection().getId() : null,
                collaborateur.getService() != null ? collaborateur.getService().getId() : null,
                collaborateur.getSection() != null ? collaborateur.getSection().getId() : null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getNomComplet() { return nomComplet; }
    public Long getDirectionId() { return directionId; }
    public Long getServiceId() { return serviceId; }
    public Long getSectionId() { return sectionId; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
