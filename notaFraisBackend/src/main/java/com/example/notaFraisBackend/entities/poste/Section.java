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
@Table(name = "Section")
@AllArgsConstructor
@Data
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Section implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String nom;

    private String description;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntite service;

    @OneToMany(mappedBy = "section")
    @JsonIgnore
    private List<Collaborateur> collaborateurs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "chef_section_id")
    private Collaborateur chefSection;

    // Constructeurs
    public Section() {}

    public Section(String code, String nom, ServiceEntite service) {
        this.code = code;
        this.nom = nom;
        this.service = service;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ServiceEntite getService() { return service; }
    public void setService(ServiceEntite service) { this.service = service; }

    public List<Collaborateur> getCollaborateurs() { return collaborateurs; }
    public void setCollaborateurs(List<Collaborateur> collaborateurs) { this.collaborateurs = collaborateurs; }

    public Collaborateur getChefSection() { return chefSection; }
    public void setChefSection(Collaborateur chefSection) { this.chefSection = chefSection; }
}
