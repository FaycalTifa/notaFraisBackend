package com.example.notaFraisBackend.entities.embedable;

import jakarta.persistence.Embeddable;

@Embeddable
public class TenuePoste {

    private Integer respectEngagements; // 1-5
    private Integer qualiteMethodesTravail;
    private Integer capacitesAdaptationOrganisation;
    private Integer encadrement;
    private Integer espritInitiativeInnovation;
    private Integer relationPresentation;
    private Integer ponctualite;
    private Integer respectReglementInterieur;

    // Détails des critères
    private String detailsRespectEngagements;
    private String detailsEspritInitiative;
    private String detailsQualiteTravail;
    private String detailsRelations;
    private String detailsPonctualite;
    private String detailsRespectReglement;
    private String detailsAdaptation;
    private String detailsEncadrement;
}
