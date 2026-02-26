package com.example.notaFraisBackend.dto.ancien;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private Long id;
    private String code;
    private String nom;
    private String description;
    private Long serviceId;
    private String serviceNom;
    private Long chefSectionId;
    private String chefSectionNom;
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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceNom() {
        return serviceNom;
    }

    public void setServiceNom(String serviceNom) {
        this.serviceNom = serviceNom;
    }

    public Long getChefSectionId() {
        return chefSectionId;
    }

    public void setChefSectionId(Long chefSectionId) {
        this.chefSectionId = chefSectionId;
    }

    public String getChefSectionNom() {
        return chefSectionNom;
    }

    public void setChefSectionNom(String chefSectionNom) {
        this.chefSectionNom = chefSectionNom;
    }

    public int getNombreCollaborateurs() {
        return nombreCollaborateurs;
    }

    public void setNombreCollaborateurs(int nombreCollaborateurs) {
        this.nombreCollaborateurs = nombreCollaborateurs;
    }
}
