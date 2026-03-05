package com.example.notaFraisBackend.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class EvaluationRequestDTO implements Serializable {

    private Integer annee;
    private LocalDate dateEntretien;
    private Long collaborateurId;

    // Section II - Faits Marquants
   /* private List<String> faitsMarquants;*/

    // Section III - Objectifs
    private List<ObjectifEvaluationDTO> objectifs;

    // Section IV - Tenue du Poste
    private Integer respectEngagements;
    private Integer qualiteMethodesTravail;
    private Integer capacitesAdaptationOrganisation;
    private Integer encadrement;
    private Integer espritInitiativeInnovation;
    private Integer relationPresentation;
    private Integer ponctualite;
    private Integer respectReglementInterieur;

    // Section V - Maîtrise du Poste
    private String niveauTechnique;
    private String niveauExperience;
    private String niveauEncadrement;
    private String commentairesMaitrise;

    // Section VI - Objectifs N+1
    private List<ObjectifFuturDTO> objectifsFuturs;

    // Section VII - Formations
    private List<SouhaitFormationDTO> souhaitsFormations;

    // Section VIII - Commentaires
    private String commentaireResponsable;
    private String commentaireCollaborateur;
    private String commentaireN2;
    private String commentaireN3;

    private List<FaitMarquantDTO> faitsMarquants;

    // Getter et Setter

    // Getters et Setters
    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }

    public LocalDate getDateEntretien() { return dateEntretien; }
    public void setDateEntretien(LocalDate dateEntretien) { this.dateEntretien = dateEntretien; }

    public Long getCollaborateurId() { return collaborateurId; }
    public void setCollaborateurId(Long collaborateurId) { this.collaborateurId = collaborateurId; }

    public List<FaitMarquantDTO> getFaitsMarquants() {
        return faitsMarquants;
    }

    public void setFaitsMarquants(List<FaitMarquantDTO> faitsMarquants) {
        this.faitsMarquants = faitsMarquants;
    }

    /*public List<String> getFaitsMarquants() { return faitsMarquants; }
        public void setFaitsMarquants(List<String> faitsMarquants) { this.faitsMarquants = faitsMarquants; }
    */
    public List<ObjectifEvaluationDTO> getObjectifs() { return objectifs; }
    public void setObjectifs(List<ObjectifEvaluationDTO> objectifs) { this.objectifs = objectifs; }

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

    public String getCommentaireResponsable() { return commentaireResponsable; }
    public void setCommentaireResponsable(String commentaireResponsable) { this.commentaireResponsable = commentaireResponsable; }

    public String getCommentaireCollaborateur() { return commentaireCollaborateur; }
    public void setCommentaireCollaborateur(String commentaireCollaborateur) { this.commentaireCollaborateur = commentaireCollaborateur; }

    public String getCommentaireN2() { return commentaireN2; }
    public void setCommentaireN2(String commentaireN2) { this.commentaireN2 = commentaireN2; }

    public String getCommentaireN3() { return commentaireN3; }
    public void setCommentaireN3(String commentaireN3) { this.commentaireN3 = commentaireN3; }
}
