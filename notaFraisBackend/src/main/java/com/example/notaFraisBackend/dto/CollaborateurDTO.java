package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.enume.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public class CollaborateurDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenoms;

    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    private String telephone;

    @Past(message = "La date d'embauche doit être dans le passé")
    private LocalDate dateEmbauche;

    private String posteActuel;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    private boolean actif = true;

    // IDs des relations
    private Long directionId;
    private String directionNom;
private String nomComplet;
private String experienceCumulee;

    private Long serviceId;
    private String serviceNom;

    private Long sectionId;
    private String sectionNom;

    private Long responsableDirectId;
    private String responsableDirectNom;

    private Long responsableHierarchiqueId;
    private String responsableHierarchiqueNom;

    // Constructeurs
    public CollaborateurDTO() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenoms() { return prenoms; }
    public void setPrenoms(String prenoms) { this.prenoms = prenoms; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getPosteActuel() { return posteActuel; }
    public void setPosteActuel(String posteActuel) { this.posteActuel = posteActuel; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public Long getDirectionId() { return directionId; }
    public void setDirectionId(Long directionId) { this.directionId = directionId; }

    public String getDirectionNom() { return directionNom; }
    public void setDirectionNom(String directionNom) { this.directionNom = directionNom; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getServiceNom() { return serviceNom; }
    public void setServiceNom(String serviceNom) { this.serviceNom = serviceNom; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

    public String getSectionNom() { return sectionNom; }
    public void setSectionNom(String sectionNom) { this.sectionNom = sectionNom; }

    public Long getResponsableDirectId() { return responsableDirectId; }
    public void setResponsableDirectId(Long responsableDirectId) { this.responsableDirectId = responsableDirectId; }

    public String getResponsableDirectNom() { return responsableDirectNom; }
    public void setResponsableDirectNom(String responsableDirectNom) { this.responsableDirectNom = responsableDirectNom; }

    public Long getResponsableHierarchiqueId() { return responsableHierarchiqueId; }
    public void setResponsableHierarchiqueId(Long responsableHierarchiqueId) { this.responsableHierarchiqueId = responsableHierarchiqueId; }

    public String getResponsableHierarchiqueNom() { return responsableHierarchiqueNom; }
    public void setResponsableHierarchiqueNom(String responsableHierarchiqueNom) { this.responsableHierarchiqueNom = responsableHierarchiqueNom; }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setExperienceCumulee(String experienceCumulee) {
        this.experienceCumulee = experienceCumulee;
    }

    public String getExperienceCumulee() {
        return experienceCumulee;
    }
}
