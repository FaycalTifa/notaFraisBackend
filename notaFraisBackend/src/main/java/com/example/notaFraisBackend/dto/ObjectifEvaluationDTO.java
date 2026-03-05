package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.enume.NiveauAtteinte;

import java.io.Serializable;

public class ObjectifEvaluationDTO implements Serializable {

    private Long id;
    private String libelle;
    private String appreciationCollaborateur;
    private String appreciationResponsable;
    private NiveauAtteinte niveauAtteinte;

    private Integer cotation;
    private Integer tauxAtteinte;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getAppreciationCollaborateur() { return appreciationCollaborateur; }
    public void setAppreciationCollaborateur(String appreciationCollaborateur) { this.appreciationCollaborateur = appreciationCollaborateur; }

    public String getAppreciationResponsable() { return appreciationResponsable; }
    public void setAppreciationResponsable(String appreciationResponsable) { this.appreciationResponsable = appreciationResponsable; }

    public NiveauAtteinte getNiveauAtteinte() { return niveauAtteinte; }
    public void setNiveauAtteinte(NiveauAtteinte niveauAtteinte) { this.niveauAtteinte = niveauAtteinte; }

    public Integer getCotation() { return cotation; }
    public void setCotation(Integer cotation) { this.cotation = cotation; }

    public Integer getTauxAtteinte() { return tauxAtteinte; }
    public void setTauxAtteinte(Integer tauxAtteinte) { this.tauxAtteinte = tauxAtteinte; }
}
