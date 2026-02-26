package com.example.notaFraisBackend.entities.enume;

public enum StatutEvaluation {

    BROUILLON("Brouillon"),
    EN_COURS("En cours de remplissage"),
    A_SIGNER("À signer"),
    VALIDEE("Validée"),
    ARCHIVEE("Archivée");

    private final String label;

    StatutEvaluation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
