package com.example.notaFraisBackend.dto.ancien;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {

    private Long id;
    private String code;
    private String nom;
    private String description;
    private Long directionId;
    private String directionNom;
    private Long chefServiceId;
    private String chefServiceNom;
    private int nombreSections;
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

    public Long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Long directionId) {
        this.directionId = directionId;
    }

    public String getDirectionNom() {
        return directionNom;
    }

    public void setDirectionNom(String directionNom) {
        this.directionNom = directionNom;
    }

    public Long getChefServiceId() {
        return chefServiceId;
    }

    public void setChefServiceId(Long chefServiceId) {
        this.chefServiceId = chefServiceId;
    }

    public String getChefServiceNom() {
        return chefServiceNom;
    }

    public void setChefServiceNom(String chefServiceNom) {
        this.chefServiceNom = chefServiceNom;
    }

    public int getNombreSections() {
        return nombreSections;
    }

    public void setNombreSections(int nombreSections) {
        this.nombreSections = nombreSections;
    }

    public int getNombreCollaborateurs() {
        return nombreCollaborateurs;
    }

    public void setNombreCollaborateurs(int nombreCollaborateurs) {
        this.nombreCollaborateurs = nombreCollaborateurs;
    }
}
