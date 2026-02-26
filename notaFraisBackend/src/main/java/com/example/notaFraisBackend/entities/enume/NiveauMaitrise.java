package com.example.notaFraisBackend.entities.enume;

public enum NiveauMaitrise {

    DEBUTANT("Débutant(e)"),
    INTERMEDIAIRE("Intermédiaire"),
    CONFIRME("Confirmé(e)"),
    EXPERT("Expert(e)");

    private final String label;

    NiveauMaitrise(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
