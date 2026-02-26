package com.example.notaFraisBackend.dto;

import com.example.notaFraisBackend.entities.enume.TypeObjectif;

public class ObjectifFuturDTO {

    private Long id;
    private String libelle;
    private String planAction;
    private String moyens;
    private String indicateursSuivi;
    private TypeObjectif type;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getPlanAction() { return planAction; }
    public void setPlanAction(String planAction) { this.planAction = planAction; }

    public String getMoyens() { return moyens; }
    public void setMoyens(String moyens) { this.moyens = moyens; }

    public String getIndicateursSuivi() { return indicateursSuivi; }
    public void setIndicateursSuivi(String indicateursSuivi) { this.indicateursSuivi = indicateursSuivi; }

    public TypeObjectif getType() { return type; }
    public void setType(TypeObjectif type) { this.type = type; }



}
