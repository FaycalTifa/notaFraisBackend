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
@Table(name = "Service")
@AllArgsConstructor
@Data
@Setter
@Getter
public class ServiceEntite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String code;

    @Column()
    private String nom;

    private String description;

    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "service")
    @JsonIgnore
    private List<Collaborateur> collaborateurs = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "chef_service_id")
    private Collaborateur chefService;

    // Constructeurs
    public ServiceEntite() {}

    public ServiceEntite(String code, String nom, Direction direction) {
        this.code = code;
        this.nom = nom;
        this.direction = direction;
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

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }

    public List<Collaborateur> getCollaborateurs() { return collaborateurs; }
    public void setCollaborateurs(List<Collaborateur> collaborateurs) { this.collaborateurs = collaborateurs; }

    public Collaborateur getChefService() { return chefService; }
    public void setChefService(Collaborateur chefService) { this.chefService = chefService; }
}
