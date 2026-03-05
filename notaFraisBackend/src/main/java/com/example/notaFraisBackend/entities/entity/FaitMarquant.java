package com.example.notaFraisBackend.entities.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "fait_marquant")
public class FaitMarquant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type; // CHANGEMENT_POSTE, MISSION_SPECIFIQUE, etc.

    @Column(name = "description")
    private String description;

    @Column(name = "date_evenement")
    private LocalDate dateEvenement;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    // Constructeurs
    public FaitMarquant() {}

    public FaitMarquant(String type, String description, LocalDate dateEvenement) {
        this.type = type;
        this.description = description;
        this.dateEvenement = dateEvenement;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateEvenement() { return dateEvenement; }
    public void setDateEvenement(LocalDate dateEvenement) { this.dateEvenement = dateEvenement; }

    public Evaluation getEvaluation() { return evaluation; }
    public void setEvaluation(Evaluation evaluation) { this.evaluation = evaluation; }
}
