package com.example.notaFraisBackend.dto.ancien;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectionDTO {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private Long directeurId;
    private String directeurNom;
    private int nombreServices;
    private int nombreCollaborateurs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDirecteurId() {
        return directeurId;
    }

    public void setDirecteurId(Long directeurId) {
        this.directeurId = directeurId;
    }

    public String getDirecteurNom() {
        return directeurNom;
    }

    public void setDirecteurNom(String directeurNom) {
        this.directeurNom = directeurNom;
    }

    public int getNombreServices() {
        return nombreServices;
    }

    public void setNombreServices(int nombreServices) {
        this.nombreServices = nombreServices;
    }

    public int getNombreCollaborateurs() {
        return nombreCollaborateurs;
    }

    public void setNombreCollaborateurs(int nombreCollaborateurs) {
        this.nombreCollaborateurs = nombreCollaborateurs;
    }
}
