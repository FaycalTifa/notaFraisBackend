package com.example.notaFraisBackend.entities.poste;

import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Direction")
@AllArgsConstructor
@Data
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Direction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String nom;

    private String description;

    @OneToMany(mappedBy = "direction")
    @JsonIgnore
    private List<ServiceEntite> services = new ArrayList<>();

    @OneToMany(mappedBy = "direction")
    @JsonIgnore
    private List<Collaborateur> collaborateurs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "directeur_id")
    private Collaborateur directeur;

    // Constructeurs
    public Direction() {
    }

    public Direction(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    // Getters et Setters
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

    public List<ServiceEntite> getServices() {
        return services;
    }

    public void setServices(List<ServiceEntite> services) {
        this.services = services;
    }

    public List<Collaborateur> getCollaborateurs() {
        return collaborateurs;
    }

    public void setCollaborateurs(List<Collaborateur> collaborateurs) {
        this.collaborateurs = collaborateurs;
    }

    public Collaborateur getDirecteur() {
        return directeur;
    }

    public void setDirecteur(Collaborateur directeur) {
        this.directeur = directeur;
    }

}
