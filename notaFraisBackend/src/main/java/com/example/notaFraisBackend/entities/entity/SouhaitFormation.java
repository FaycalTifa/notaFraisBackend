package com.example.notaFraisBackend.entities.entity;


import com.example.notaFraisBackend.entities.entity.Evaluation;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "souhaits_formation")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SouhaitFormation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theme")
    private String theme;

    @Column(name = "objectifs")
    private String objectifs;

    @Column(name = "resultatsAttendus")
    private String resultatsAttendus;

    @Column(name = "delaisEvaluation")
    private String delaisEvaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getObjectifs() {
        return objectifs;
    }

    public void setObjectifs(String objectifs) {
        this.objectifs = objectifs;
    }

    public String getResultatsAttendus() {
        return resultatsAttendus;
    }

    public void setResultatsAttendus(String resultatsAttendus) {
        this.resultatsAttendus = resultatsAttendus;
    }

    public String getDelaisEvaluation() {
        return delaisEvaluation;
    }

    public void setDelaisEvaluation(String delaisEvaluation) {
        this.delaisEvaluation = delaisEvaluation;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }
}
