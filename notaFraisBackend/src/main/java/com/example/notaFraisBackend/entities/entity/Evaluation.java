package com.example.notaFraisBackend.entities.entity;


import com.example.notaFraisBackend.entities.enume.NiveauMaitrise;
import com.example.notaFraisBackend.entities.enume.StatutEvaluation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Evaluation")
@AllArgsConstructor
@Data
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_entretien")
    private LocalDate dateEntretien;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutEvaluation statut;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborateur_id")
    private Collaborateur collaborateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluateur_id")
    private Collaborateur evaluateur;

    // Section II - Faits Marquants
    @ElementCollection
    @CollectionTable(name = "evaluation_faits_marquants",
            joinColumns = @JoinColumn(name = "evaluation_id"))
    @Column(name = "fait", length = 500)
    private List<String> faitsMarquants = new ArrayList<>();

    // Section III - Objectifs
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectifEvaluation> objectifs = new ArrayList<>();

    @Column(name = "note_globale_objectifs")
    private Double noteGlobaleObjectifs;

    // Section IV - Tenue du Poste
    @Column(name = "respect_engagements")
    private Integer respectEngagements;

    @Column(name = "qualite_methodes_travail")
    private Integer qualiteMethodesTravail;

    @Column(name = "capacites_adaptation_organisation")
    private Integer capacitesAdaptationOrganisation;

    @Column(name = "encadrement")
    private Integer encadrement;

    @Column(name = "esprit_initiative_innovation")
    private Integer espritInitiativeInnovation;

    @Column(name = "relation_presentation")
    private Integer relationPresentation;

    @Column(name = "ponctualite")
    private Integer ponctualite;

    @Column(name = "respect_reglement_interieur")
    private Integer respectReglementInterieur;

    @Column(name = "note_globale_tenue_poste")
    private Double noteGlobaleTenuePoste;

    // Section V - Maîtrise du Poste
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_technique", length = 50)
    private NiveauMaitrise niveauTechnique;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_experience", length = 50)
    private NiveauMaitrise niveauExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_encadrement", length = 50)
    private NiveauMaitrise niveauEncadrement;

    @Column(name = "commentaires_maitrise", length = 1000)
    private String commentairesMaitrise;

    // Section VI - Objectifs N+1
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObjectifFutur> objectifsFuturs = new ArrayList<>();

    // Section VII - Formations
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SouhaitFormation> souhaitsFormations = new ArrayList<>();

    // Section VIII - Évaluation Globale
    @Column(name = "note_globale_finale")
    private Double noteGlobaleFinale;

    @Column(name = "commentaire_responsable")
    private String commentaireResponsable;

    @Column(name = "commentaire_collaborateur")
    private String commentaireCollaborateur;

    @Column(name = "commentaire_n2")
    private String commentaireN2;

    @Column(name = "commentaire_n3")
    private String commentaireN3;

    @Column(name = "signature_responsable")
    private String signatureResponsable;

    @Column(name = "signature_collaborateur")
    private String signatureCollaborateur;

    // Notes antérieures
    @ElementCollection
    @CollectionTable(name = "evaluation_notes_anterieures",
            joinColumns = @JoinColumn(name = "evaluation_id"))
    @Column(name = "note")
    private List<Double> notesAnterieures = new ArrayList<>();

    public Evaluation() {

    }

    public Evaluation(Collaborateur collaborateur, Collaborateur evaluateur) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateEntretien() {
        return dateEntretien;
    }

    public void setDateEntretien(LocalDate dateEntretien) {
        this.dateEntretien = dateEntretien;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public StatutEvaluation getStatut() {
        return statut;
    }

    public void setStatut(StatutEvaluation statut) {
        this.statut = statut;
    }

    public Collaborateur getCollaborateur() {
        return collaborateur;
    }

    public void setCollaborateur(Collaborateur collaborateur) {
        this.collaborateur = collaborateur;
    }

    public Collaborateur getEvaluateur() {
        return evaluateur;
    }

    public void setEvaluateur(Collaborateur evaluateur) {
        this.evaluateur = evaluateur;
    }

    public List<String> getFaitsMarquants() {
        return faitsMarquants;
    }

    public void setFaitsMarquants(List<String> faitsMarquants) {
        this.faitsMarquants = faitsMarquants;
    }

    public List<ObjectifEvaluation> getObjectifs() {
        return objectifs;
    }

    public void setObjectifs(List<ObjectifEvaluation> objectifs) {
        this.objectifs = objectifs;
    }

    public Double getNoteGlobaleObjectifs() {
        return noteGlobaleObjectifs;
    }

    public void setNoteGlobaleObjectifs(Double noteGlobaleObjectifs) {
        this.noteGlobaleObjectifs = noteGlobaleObjectifs;
    }

    public Integer getRespectEngagements() {
        return respectEngagements;
    }

    public void setRespectEngagements(Integer respectEngagements) {
        this.respectEngagements = respectEngagements;
    }

    public Integer getQualiteMethodesTravail() {
        return qualiteMethodesTravail;
    }

    public void setQualiteMethodesTravail(Integer qualiteMethodesTravail) {
        this.qualiteMethodesTravail = qualiteMethodesTravail;
    }

    public Integer getCapacitesAdaptationOrganisation() {
        return capacitesAdaptationOrganisation;
    }

    public void setCapacitesAdaptationOrganisation(Integer capacitesAdaptationOrganisation) {
        this.capacitesAdaptationOrganisation = capacitesAdaptationOrganisation;
    }

    public Integer getEncadrement() {
        return encadrement;
    }

    public void setEncadrement(Integer encadrement) {
        this.encadrement = encadrement;
    }

    public Integer getEspritInitiativeInnovation() {
        return espritInitiativeInnovation;
    }

    public void setEspritInitiativeInnovation(Integer espritInitiativeInnovation) {
        this.espritInitiativeInnovation = espritInitiativeInnovation;
    }

    public Integer getRelationPresentation() {
        return relationPresentation;
    }

    public void setRelationPresentation(Integer relationPresentation) {
        this.relationPresentation = relationPresentation;
    }

    public Integer getPonctualite() {
        return ponctualite;
    }

    public void setPonctualite(Integer ponctualite) {
        this.ponctualite = ponctualite;
    }

    public Integer getRespectReglementInterieur() {
        return respectReglementInterieur;
    }

    public void setRespectReglementInterieur(Integer respectReglementInterieur) {
        this.respectReglementInterieur = respectReglementInterieur;
    }

    public Double getNoteGlobaleTenuePoste() {
        return noteGlobaleTenuePoste;
    }

    public void setNoteGlobaleTenuePoste(Double noteGlobaleTenuePoste) {
        this.noteGlobaleTenuePoste = noteGlobaleTenuePoste;
    }

    public NiveauMaitrise getNiveauTechnique() {
        return niveauTechnique;
    }

    public void setNiveauTechnique(NiveauMaitrise niveauTechnique) {
        this.niveauTechnique = niveauTechnique;
    }

    public NiveauMaitrise getNiveauExperience() {
        return niveauExperience;
    }

    public void setNiveauExperience(NiveauMaitrise niveauExperience) {
        this.niveauExperience = niveauExperience;
    }

    public NiveauMaitrise getNiveauEncadrement() {
        return niveauEncadrement;
    }

    public void setNiveauEncadrement(NiveauMaitrise niveauEncadrement) {
        this.niveauEncadrement = niveauEncadrement;
    }

    public String getCommentairesMaitrise() {
        return commentairesMaitrise;
    }

    public void setCommentairesMaitrise(String commentairesMaitrise) {
        this.commentairesMaitrise = commentairesMaitrise;
    }

    public List<ObjectifFutur> getObjectifsFuturs() {
        return objectifsFuturs;
    }

    public void setObjectifsFuturs(List<ObjectifFutur> objectifsFuturs) {
        this.objectifsFuturs = objectifsFuturs;
    }

    public List<SouhaitFormation> getSouhaitsFormations() {
        return souhaitsFormations;
    }

    public void setSouhaitsFormations(List<SouhaitFormation> souhaitsFormations) {
        this.souhaitsFormations = souhaitsFormations;
    }

    public Double getNoteGlobaleFinale() {
        return noteGlobaleFinale;
    }

    public void setNoteGlobaleFinale(Double noteGlobaleFinale) {
        this.noteGlobaleFinale = noteGlobaleFinale;
    }

    public String getCommentaireResponsable() {
        return commentaireResponsable;
    }

    public void setCommentaireResponsable(String commentaireResponsable) {
        this.commentaireResponsable = commentaireResponsable;
    }

    public String getCommentaireCollaborateur() {
        return commentaireCollaborateur;
    }

    public void setCommentaireCollaborateur(String commentaireCollaborateur) {
        this.commentaireCollaborateur = commentaireCollaborateur;
    }

    public String getCommentaireN2() {
        return commentaireN2;
    }

    public void setCommentaireN2(String commentaireN2) {
        this.commentaireN2 = commentaireN2;
    }

    public String getCommentaireN3() {
        return commentaireN3;
    }

    public void setCommentaireN3(String commentaireN3) {
        this.commentaireN3 = commentaireN3;
    }

    public String getSignatureResponsable() {
        return signatureResponsable;
    }

    public void setSignatureResponsable(String signatureResponsable) {
        this.signatureResponsable = signatureResponsable;
    }

    public String getSignatureCollaborateur() {
        return signatureCollaborateur;
    }

    public void setSignatureCollaborateur(String signatureCollaborateur) {
        this.signatureCollaborateur = signatureCollaborateur;
    }

    public List<Double> getNotesAnterieures() {
        return notesAnterieures;
    }

    public void setNotesAnterieures(List<Double> notesAnterieures) {
        this.notesAnterieures = notesAnterieures;
    }




    /**
     * Calcule les notes de l'évaluation :
     * - Note Objectifs (moyenne des cotations)
     * - Note Tenue du poste (moyenne des notes convertie sur 10)
     * - Note finale (60% objectifs + 40% tenue)
     */
    public void calculerNotes() {
        System.out.println("=== CALCUL DES NOTES ===");

        // 1. Calcul de la note des objectifs (moyenne des cotations sur 10)
        if (this.objectifs != null && !this.objectifs.isEmpty()) {
            double sommeObjectifs = 0;
            int nbObjectifsAvecCotation = 0;

            for (ObjectifEvaluation obj : this.objectifs) {
                if (obj.getCotation() != null) {
                    sommeObjectifs += obj.getCotation();
                    nbObjectifsAvecCotation++;
                    System.out.println("Objectif - Cotation: " + obj.getCotation());
                }
            }

            if (nbObjectifsAvecCotation > 0) {
                this.noteGlobaleObjectifs = sommeObjectifs / nbObjectifsAvecCotation;
                System.out.println("Note Objectifs calculée: " + this.noteGlobaleObjectifs + "/10");
            } else {
                this.noteGlobaleObjectifs = null;
                System.out.println("Aucun objectif avec cotation");
            }
        } else {
            this.noteGlobaleObjectifs = null;
            System.out.println("Aucun objectif");
        }

        // 2. Calcul de la note de tenue du poste (moyenne des notes sur 10)
        double sommeTenue = 0;
        int nbCriteres = 0;

        if (this.respectEngagements != null) {
            sommeTenue += this.respectEngagements;
            nbCriteres++;
            System.out.println("Respect engagements: " + this.respectEngagements);
        }
        if (this.qualiteMethodesTravail != null) {
            sommeTenue += this.qualiteMethodesTravail;
            nbCriteres++;
            System.out.println("Qualité méthodes: " + this.qualiteMethodesTravail);
        }
        if (this.capacitesAdaptationOrganisation != null) {
            sommeTenue += this.capacitesAdaptationOrganisation;
            nbCriteres++;
            System.out.println("Capacités adaptation: " + this.capacitesAdaptationOrganisation);
        }
        if (this.encadrement != null) {
            sommeTenue += this.encadrement;
            nbCriteres++;
            System.out.println("Encadrement: " + this.encadrement);
        }
        if (this.espritInitiativeInnovation != null) {
            sommeTenue += this.espritInitiativeInnovation;
            nbCriteres++;
            System.out.println("Esprit initiative: " + this.espritInitiativeInnovation);
        }
        if (this.relationPresentation != null) {
            sommeTenue += this.relationPresentation;
            nbCriteres++;
            System.out.println("Relation présentation: " + this.relationPresentation);
        }
        if (this.ponctualite != null) {
            sommeTenue += this.ponctualite;
            nbCriteres++;
            System.out.println("Ponctualité: " + this.ponctualite);
        }
        if (this.respectReglementInterieur != null) {
            sommeTenue += this.respectReglementInterieur;
            nbCriteres++;
            System.out.println("Respect règlement: " + this.respectReglementInterieur);
        }

        if (nbCriteres > 0) {
            this.noteGlobaleTenuePoste = sommeTenue / nbCriteres;
            this.noteGlobaleTenuePoste = Math.round(this.noteGlobaleTenuePoste * 10.0) / 10.0;
            System.out.println("Note Tenue calculée: " + this.noteGlobaleTenuePoste + "/10 (sur " + nbCriteres + " critères)");
        } else {
            this.noteGlobaleTenuePoste = null;
            System.out.println("Aucun critère de tenue");
        }

        // 3. Calcul de la note finale (60% objectifs + 40% tenue)
        // CORRECTION: Si les objectifs sont absents, utiliser seulement la tenue
        if (this.noteGlobaleObjectifs != null && this.noteGlobaleTenuePoste != null) {
            this.noteGlobaleFinale = (this.noteGlobaleObjectifs * 0.6) + (this.noteGlobaleTenuePoste * 0.4);
            System.out.println("Note Finale calculée (avec objectifs): " + this.noteGlobaleFinale);
        } else if (this.noteGlobaleObjectifs == null && this.noteGlobaleTenuePoste != null) {
            // Cas où il n'y a que la tenue du poste
            this.noteGlobaleFinale = this.noteGlobaleTenuePoste;
            System.out.println("Note Finale calculée (tenue seulement): " + this.noteGlobaleFinale);
        } else if (this.noteGlobaleObjectifs != null && this.noteGlobaleTenuePoste == null) {
            // Cas où il n'y a que les objectifs
            this.noteGlobaleFinale = this.noteGlobaleObjectifs;
            System.out.println("Note Finale calculée (objectifs seulement): " + this.noteGlobaleFinale);
        } else {
            this.noteGlobaleFinale = null;
            System.out.println("Note finale non calculée (aucune note disponible)");
        }

        // Arrondir la note finale si elle existe
        if (this.noteGlobaleFinale != null) {
            this.noteGlobaleFinale = Math.round(this.noteGlobaleFinale * 10.0) / 10.0;
        }

        System.out.println("=== FIN CALCUL ===\n");
    }


    }
