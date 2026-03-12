package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.entity.Evaluation;
import com.example.notaFraisBackend.entities.enume.StatutEvaluation;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationDTO implements Serializable {


    private Long id;
    private Integer annee;
    private LocalDate dateCreation;
    private LocalDate dateEntretien;
    private LocalDate dateValidation;
    private StatutEvaluation statut;

    // Relations
    private Long collaborateurId;
    private String collaborateurNom;
    private Long evaluateurId;
    private String evaluateurNom;

    // Section II - Faits Marquants
    private List<FaitMarquantDTO> faitsMarquants;

    // Section III - Objectifs
    private List<ObjectifEvaluationDTO> objectifs;
    private Double noteGlobaleObjectifs;

    // Section IV - Tenue du Poste
    private Integer respectEngagements;
    private Integer qualiteMethodesTravail;
    private Integer capacitesAdaptationOrganisation;
    private Integer encadrement;
    private Integer espritInitiativeInnovation;
    private Integer relationPresentation;
    private Integer ponctualite;
    private Integer respectReglementInterieur;
    private Double noteGlobaleTenuePoste;

    // Section V - Maîtrise du Poste
    private String niveauTechnique;
    private String niveauExperience;
    private String niveauEncadrement;
    private String commentairesMaitrise;

    // Section VI - Objectifs N+1
    private List<ObjectifFuturDTO> objectifsFuturs;

    // Section VII - Formations
    private List<SouhaitFormationDTO> souhaitsFormations;

    // Section VIII - Évaluation Globale
    private Double noteGlobaleFinale;
    private String commentaireResponsable;
    private String commentaireCollaborateur;
    private String commentaireN2;
    private String commentaireN3;
    private String signatureResponsable;
    private String signatureCollaborateur;

    private String motifRefus;

    private Long refuseParId;
    private String refuseParNom;

    private LocalDate dateRefus;
    // Notes antérieures
    private List<Double> notesAnterieures;

    // ✅ AJOUTER CES CHAMPS
    private String motifAnnulation;
    private LocalDate dateAnnulation;
    private String annulePar; // ou un objet CollaborateurDTO
    private Long annuleParId;

    // AJOUTER CES CHAMPS
    private CollaborateurDTO collaborateur;
    private CollaborateurDTO evaluateur;

    public EvaluationDTO() {

    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMotifRefus() {
        return motifRefus;
    }

    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public Long getRefuseParId() {
        return refuseParId;
    }

    public void setRefuseParId(Long refuseParId) {
        this.refuseParId = refuseParId;
    }

    public String getRefuseParNom() {
        return refuseParNom;
    }

    public void setRefuseParNom(String refuseParNom) {
        this.refuseParNom = refuseParNom;
    }

    public LocalDate getDateRefus() {
        return dateRefus;
    }

    public void setDateRefus(LocalDate dateRefus) {
        this.dateRefus = dateRefus;
    }

    public String getMotifAnnulation() {
        return motifAnnulation;
    }

    public void setMotifAnnulation(String motifAnnulation) {
        this.motifAnnulation = motifAnnulation;
    }

    public LocalDate getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(LocalDate dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    public String getAnnulePar() {
        return annulePar;
    }

    public void setAnnulePar(String annulePar) {
        this.annulePar = annulePar;
    }

    public Long getAnnuleParId() {
        return annuleParId;
    }

    public void setAnnuleParId(Long annuleParId) {
        this.annuleParId = annuleParId;
    }

    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateEntretien() { return dateEntretien; }
    public void setDateEntretien(LocalDate dateEntretien) { this.dateEntretien = dateEntretien; }

    public LocalDate getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDate dateValidation) { this.dateValidation = dateValidation; }

    public StatutEvaluation getStatut() { return statut; }
    public void setStatut(StatutEvaluation statut) { this.statut = statut; }

    public Long getCollaborateurId() { return collaborateurId; }
    public void setCollaborateurId(Long collaborateurId) { this.collaborateurId = collaborateurId; }

    public String getCollaborateurNom() { return collaborateurNom; }
    public void setCollaborateurNom(String collaborateurNom) { this.collaborateurNom = collaborateurNom; }

    public Long getEvaluateurId() { return evaluateurId; }
    public void setEvaluateurId(Long evaluateurId) { this.evaluateurId = evaluateurId; }

    public String getEvaluateurNom() { return evaluateurNom; }
    public void setEvaluateurNom(String evaluateurNom) { this.evaluateurNom = evaluateurNom; }

    public List<FaitMarquantDTO> getFaitsMarquants() {
        return faitsMarquants;
    }

    public void setFaitsMarquants(List<FaitMarquantDTO> faitsMarquants) {
        this.faitsMarquants = faitsMarquants;
    }

    public List<ObjectifEvaluationDTO> getObjectifs() { return objectifs; }
    public void setObjectifs(List<ObjectifEvaluationDTO> objectifs) { this.objectifs = objectifs; }

    public Double getNoteGlobaleObjectifs() { return noteGlobaleObjectifs; }
    public void setNoteGlobaleObjectifs(Double noteGlobaleObjectifs) { this.noteGlobaleObjectifs = noteGlobaleObjectifs; }

    public Integer getRespectEngagements() { return respectEngagements; }
    public void setRespectEngagements(Integer respectEngagements) { this.respectEngagements = respectEngagements; }

    public Integer getQualiteMethodesTravail() { return qualiteMethodesTravail; }
    public void setQualiteMethodesTravail(Integer qualiteMethodesTravail) { this.qualiteMethodesTravail = qualiteMethodesTravail; }

    public Integer getCapacitesAdaptationOrganisation() { return capacitesAdaptationOrganisation; }
    public void setCapacitesAdaptationOrganisation(Integer capacitesAdaptationOrganisation) { this.capacitesAdaptationOrganisation = capacitesAdaptationOrganisation; }

    public Integer getEncadrement() { return encadrement; }
    public void setEncadrement(Integer encadrement) { this.encadrement = encadrement; }

    public Integer getEspritInitiativeInnovation() { return espritInitiativeInnovation; }
    public void setEspritInitiativeInnovation(Integer espritInitiativeInnovation) { this.espritInitiativeInnovation = espritInitiativeInnovation; }

    public Integer getRelationPresentation() { return relationPresentation; }
    public void setRelationPresentation(Integer relationPresentation) { this.relationPresentation = relationPresentation; }

    public Integer getPonctualite() { return ponctualite; }
    public void setPonctualite(Integer ponctualite) { this.ponctualite = ponctualite; }

    public Integer getRespectReglementInterieur() { return respectReglementInterieur; }
    public void setRespectReglementInterieur(Integer respectReglementInterieur) { this.respectReglementInterieur = respectReglementInterieur; }

    public Double getNoteGlobaleTenuePoste() { return noteGlobaleTenuePoste; }
    public void setNoteGlobaleTenuePoste(Double noteGlobaleTenuePoste) { this.noteGlobaleTenuePoste = noteGlobaleTenuePoste; }

    public String getNiveauTechnique() { return niveauTechnique; }
    public void setNiveauTechnique(String niveauTechnique) { this.niveauTechnique = niveauTechnique; }

    public String getNiveauExperience() { return niveauExperience; }
    public void setNiveauExperience(String niveauExperience) { this.niveauExperience = niveauExperience; }

    public String getNiveauEncadrement() { return niveauEncadrement; }
    public void setNiveauEncadrement(String niveauEncadrement) { this.niveauEncadrement = niveauEncadrement; }

    public String getCommentairesMaitrise() { return commentairesMaitrise; }
    public void setCommentairesMaitrise(String commentairesMaitrise) { this.commentairesMaitrise = commentairesMaitrise; }

    public List<ObjectifFuturDTO> getObjectifsFuturs() { return objectifsFuturs; }
    public void setObjectifsFuturs(List<ObjectifFuturDTO> objectifsFuturs) { this.objectifsFuturs = objectifsFuturs; }

    public List<SouhaitFormationDTO> getSouhaitsFormations() { return souhaitsFormations; }
    public void setSouhaitsFormations(List<SouhaitFormationDTO> souhaitsFormations) { this.souhaitsFormations = souhaitsFormations; }

    public Double getNoteGlobaleFinale() { return noteGlobaleFinale; }
    public void setNoteGlobaleFinale(Double noteGlobaleFinale) { this.noteGlobaleFinale = noteGlobaleFinale; }

    public String getCommentaireResponsable() { return commentaireResponsable; }
    public void setCommentaireResponsable(String commentaireResponsable) { this.commentaireResponsable = commentaireResponsable; }

    public String getCommentaireCollaborateur() { return commentaireCollaborateur; }
    public void setCommentaireCollaborateur(String commentaireCollaborateur) { this.commentaireCollaborateur = commentaireCollaborateur; }

    public String getCommentaireN2() { return commentaireN2; }
    public void setCommentaireN2(String commentaireN2) { this.commentaireN2 = commentaireN2; }

    public String getCommentaireN3() { return commentaireN3; }
    public void setCommentaireN3(String commentaireN3) { this.commentaireN3 = commentaireN3; }

    public String getSignatureResponsable() { return signatureResponsable; }
    public void setSignatureResponsable(String signatureResponsable) { this.signatureResponsable = signatureResponsable; }

    public String getSignatureCollaborateur() { return signatureCollaborateur; }
    public void setSignatureCollaborateur(String signatureCollaborateur) { this.signatureCollaborateur = signatureCollaborateur; }

    public List<Double> getNotesAnterieures() { return notesAnterieures; }

    public CollaborateurDTO getCollaborateur() {
        return collaborateur;
    }

    public void setCollaborateur(CollaborateurDTO collaborateur) {
        this.collaborateur = collaborateur;
    }

    public CollaborateurDTO getEvaluateur() {
        return evaluateur;
    }

    public void setEvaluateur(CollaborateurDTO evaluateur) {
        this.evaluateur = evaluateur;
    }

    public void setNotesAnterieures(List<Double> notesAnterieures) { this.notesAnterieures = notesAnterieures; }

    public EvaluationDTO(Long id, Integer annee, LocalDate dateCreation, LocalDate dateEntretien, LocalDate dateValidation, StatutEvaluation statut, Long collaborateurId, String collaborateurNom, Long evaluateurId, String evaluateurNom, List<FaitMarquantDTO> faitsMarquants, List<ObjectifEvaluationDTO> objectifs, Double noteGlobaleObjectifs, Integer respectEngagements, Integer qualiteMethodesTravail, Integer capacitesAdaptationOrganisation, Integer encadrement, Integer espritInitiativeInnovation, Integer relationPresentation, Integer ponctualite, Integer respectReglementInterieur, Double noteGlobaleTenuePoste, String niveauTechnique, String niveauExperience, String niveauEncadrement, String commentairesMaitrise, List<ObjectifFuturDTO> objectifsFuturs, List<SouhaitFormationDTO> souhaitsFormations, Double noteGlobaleFinale, String commentaireResponsable, String commentaireCollaborateur, String commentaireN2, String commentaireN3, String signatureResponsable, String signatureCollaborateur, List<Double> notesAnterieures, CollaborateurDTO collaborateur, CollaborateurDTO evaluateur) {
        this.id = id;
        this.annee = annee;
        this.dateCreation = dateCreation;
        this.dateEntretien = dateEntretien;
        this.dateValidation = dateValidation;
        this.statut = statut;
        this.collaborateurId = collaborateurId;
        this.collaborateurNom = collaborateurNom;
        this.evaluateurId = evaluateurId;
        this.evaluateurNom = evaluateurNom;
        this.faitsMarquants = faitsMarquants;
        this.objectifs = objectifs;
        this.noteGlobaleObjectifs = noteGlobaleObjectifs;
        this.respectEngagements = respectEngagements;
        this.qualiteMethodesTravail = qualiteMethodesTravail;
        this.capacitesAdaptationOrganisation = capacitesAdaptationOrganisation;
        this.encadrement = encadrement;
        this.espritInitiativeInnovation = espritInitiativeInnovation;
        this.relationPresentation = relationPresentation;
        this.ponctualite = ponctualite;
        this.respectReglementInterieur = respectReglementInterieur;
        this.noteGlobaleTenuePoste = noteGlobaleTenuePoste;
        this.niveauTechnique = niveauTechnique;
        this.niveauExperience = niveauExperience;
        this.niveauEncadrement = niveauEncadrement;
        this.commentairesMaitrise = commentairesMaitrise;
        this.objectifsFuturs = objectifsFuturs;
        this.souhaitsFormations = souhaitsFormations;
        this.noteGlobaleFinale = noteGlobaleFinale;
        this.commentaireResponsable = commentaireResponsable;
        this.commentaireCollaborateur = commentaireCollaborateur;
        this.commentaireN2 = commentaireN2;
        this.commentaireN3 = commentaireN3;
        this.signatureResponsable = signatureResponsable;
        this.signatureCollaborateur = signatureCollaborateur;
        this.notesAnterieures = notesAnterieures;
        this.collaborateur = collaborateur;
        this.evaluateur = evaluateur;
    }
}
