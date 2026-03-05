package com.example.notaFraisBackend.entities.entity;


import com.example.notaFraisBackend.entities.enume.NiveauAtteinte;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "objectifs_evaluation")
@Data
@Setter
@Getter
public class ObjectifEvaluation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "appreciationCollaborateur")
    private String appreciationCollaborateur;

    @Column(name = "appreciationResponsable")
    private String appreciationResponsable;

    @Enumerated(EnumType.STRING)
    private NiveauAtteinte niveauAtteinte;



    @Column(name = "cotation")
    private Integer cotation; // 0-10

    @Column(name = "tauxAtteinte")
    private Integer  tauxAtteinte; // Pourcentage

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAppreciationCollaborateur() {
        return appreciationCollaborateur;
    }

    public void setAppreciationCollaborateur(String appreciationCollaborateur) {
        this.appreciationCollaborateur = appreciationCollaborateur;
    }

    public String getAppreciationResponsable() {
        return appreciationResponsable;
    }

    public void setAppreciationResponsable(String appreciationResponsable) {
        this.appreciationResponsable = appreciationResponsable;
    }

    public NiveauAtteinte getNiveauAtteinte() {
        return niveauAtteinte;
    }

    public void setNiveauAtteinte(NiveauAtteinte niveauAtteinte) {
        this.niveauAtteinte = niveauAtteinte;
    }

    public Integer getCotation() {
        return cotation;
    }

    public void setCotation(Integer cotation) {
        this.cotation = cotation;
    }

    public Integer getTauxAtteinte() {
        return tauxAtteinte;
    }

    public void setTauxAtteinte(Integer tauxAtteinte) {
        this.tauxAtteinte = tauxAtteinte;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }



    public ObjectifEvaluation(String s) {
    }

    public ObjectifEvaluation(Long id, String libelle, String appreciationCollaborateur, String appreciationResponsable, NiveauAtteinte niveauAtteinte, Integer cotation, Integer tauxAtteinte, Evaluation evaluation) {
        this.id = id;
        this.libelle = libelle;
        this.appreciationCollaborateur = appreciationCollaborateur;
        this.appreciationResponsable = appreciationResponsable;
        this.niveauAtteinte = niveauAtteinte;
        this.cotation = cotation;
        this.tauxAtteinte = tauxAtteinte;
        this.evaluation = evaluation;
    }

    public ObjectifEvaluation() {
    }
}
