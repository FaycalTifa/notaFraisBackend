package com.example.notaFraisBackend.entities;

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
@NoArgsConstructor
@Data
@Setter
@Getter
public class ServiceEntite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Code")
    private String code;
    @Column(name = "Libelle")
    private String libelle;
    @Column(name = "isDeleted")
    private boolean isDeleted = false;
    // Relation Many-to-One avec Direction
    // Relation Many-to-One avec Direction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "services"}) // ← Modifié
    private Direction direction;

    // Relation One-to-Many avec Section
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // ← Gardez
    private List<Section> sections = new ArrayList<>();

    // Champ transient
    @Transient
    private Long directionId;

    public Long getId() {
        return id;
    }

    public Long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Long directionId) {
        this.directionId = directionId;
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
