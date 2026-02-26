package com.example.notaFraisBackend.entities.enume;

public enum TypeObjectif {

    ANNUEL("Objectifs Annuels"),
    TENUE_POSTE("Objectifs liés à la bonne tenue du Poste");

    private final String label;

    TypeObjectif(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
