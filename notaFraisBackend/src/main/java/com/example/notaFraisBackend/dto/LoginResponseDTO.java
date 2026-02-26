package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.enume.Role;

public class LoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String nomComplet;
    private String role;
    private String direction;
    private String service;
    private String section;

    // Constructeur par défaut
    public LoginResponseDTO() {}

    // Constructeur avec paramètres
    public LoginResponseDTO(String token, Long id, String email, String nomComplet, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nomComplet = nomComplet;
        this.role = role;
    }

    // Getters et Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
}
