package com.example.notaFraisBackend.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Evaluation")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Evaluation implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "niveau")  // ✅ CORRECT: même nom que la colonne en base
    private String niveau;

    @Column(name = "ideleted") // ✅ CORRECT: nom réel de la colonne
    private Boolean isDeleted  = false; // ✅ Boolean wrapper au lieu de primitive

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
