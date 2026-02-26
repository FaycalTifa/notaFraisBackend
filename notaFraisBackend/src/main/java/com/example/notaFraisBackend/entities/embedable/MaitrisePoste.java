package com.example.notaFraisBackend.entities.embedable;

import com.example.notaFraisBackend.entities.enume.NiveauMaitrise;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class MaitrisePoste {

    @Enumerated(EnumType.STRING)
    private NiveauMaitrise niveauTechnique; // DEBUTANT, INTERMEDIAIRE, CONFIRME, EXPERT

    @Enumerated(EnumType.STRING)
    private NiveauMaitrise niveauExperience;

    @Enumerated(EnumType.STRING)
    private NiveauMaitrise niveauEncadrement;

    private String commentaires;
}
