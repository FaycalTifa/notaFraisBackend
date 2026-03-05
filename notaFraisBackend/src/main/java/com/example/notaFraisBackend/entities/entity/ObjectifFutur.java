package com.example.notaFraisBackend.entities.entity;

import com.example.notaFraisBackend.entities.enume.TypeObjectif;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "objectifs_futurs")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ObjectifFutur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "planAction")
    private String planAction;

    @Column(name = "moyens")
    private String moyens;

    @Column(name = "indicateursSuivi")
    private String indicateursSuivi;

    @Enumerated(EnumType.STRING)
    private TypeObjectif type;

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

    public String getPlanAction() {
        return planAction;
    }

    public void setPlanAction(String planAction) {
        this.planAction = planAction;
    }

    public String getMoyens() {
        return moyens;
    }

    public void setMoyens(String moyens) {
        this.moyens = moyens;
    }

    public String getIndicateursSuivi() {
        return indicateursSuivi;
    }

    public void setIndicateursSuivi(String indicateursSuivi) {
        this.indicateursSuivi = indicateursSuivi;
    }

    public TypeObjectif getType() {
        return type;
    }

    public void setType(TypeObjectif type) {
        this.type = type;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
