package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.enume.Role;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

public class CollaborateurRequestDTO implements Serializable {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenoms;

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(min = 3, max = 20, message = "Le matricule doit contenir entre 3 et 20 caractères")
    private String matricule;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String telephone;

    @Past(message = "La date d'embauche doit être dans le passé")
    private LocalDate dateEmbauche;

    private String posteActuel;

    @NotNull(message = "Le rôle est obligatoire")
    private Role role;

    private Long directionId;
    private Long serviceId;
    private Long sectionId;
    private Long responsableDirectId;

    // NOUVEAU CHAMP POUR LA SIGNATURE
    private String signature;

    private String signatureFilename;
    private String signatureContentType;

    // Getters et Setters


    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureFilename() {
        return signatureFilename;
    }

    public void setSignatureFilename(String signatureFilename) {
        this.signatureFilename = signatureFilename;
    }

    public String getSignatureContentType() {
        return signatureContentType;
    }

    public void setSignatureContentType(String signatureContentType) {
        this.signatureContentType = signatureContentType;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenoms() { return prenoms; }
    public void setPrenoms(String prenoms) { this.prenoms = prenoms; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getPosteActuel() { return posteActuel; }
    public void setPosteActuel(String posteActuel) { this.posteActuel = posteActuel; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getDirectionId() { return directionId; }
    public void setDirectionId(Long directionId) { this.directionId = directionId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

    public Long getResponsableDirectId() { return responsableDirectId; }
    public void setResponsableDirectId(Long responsableDirectId) { this.responsableDirectId = responsableDirectId; }
}

