package com.example.notaFraisBackend.dto;

import javax.validation.constraints.NotBlank;

public class AnnulationRequestDTO {

    @NotBlank(message = "Le motif d'annulation est obligatoire")
    private String motif;

    private String commentaire;

    // Getters et Setters
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
