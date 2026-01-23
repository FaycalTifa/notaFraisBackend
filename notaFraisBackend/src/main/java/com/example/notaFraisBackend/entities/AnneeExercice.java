package com.example.notaFraisBackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "AnneeExercice")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AnneeExercice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annee")  // ✅ CORRECT: même nom que la colonne en base
    private Long annee;

    @Column(name = "is_actived") // ✅ CORRECT: nom réel de la colonne
    private Boolean isActived = false; // ✅ Boolean wrapper au lieu de primitive

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnnee() {
        return annee;
    }

    public void setAnnee(Long annee) {
        this.annee = annee;
    }

    public Boolean getActived() {
        return isActived;
    }

    public void setActived(Boolean actived) {
        isActived = actived;
    }

    @Override
    public String toString() {
        return "AnneeExercice{" +
                "id=" + id +
                ", annee=" + annee +
                ", isActived=" + isActived +
                '}';
    }


}
