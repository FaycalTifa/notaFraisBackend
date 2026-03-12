package com.example.notaFraisBackend.entities.enume;

public enum StatutEvaluation {

    BROUILLON("Brouillon"),
    A_APPROUVER("À approuver par le collaborateur"),
    APPROUVEE("Approuvée par le collaborateur"),
    A_VALIDER_SERVICE("À valider par le chef de service"),
    A_VALIDER_DIRECTEUR("À valider par le directeur"),
    EN_COURS("En cours de validation"),
    VALIDEE("Validée"),
    REFUSEE("Refusée"),
    ANNULEE ("Annulée");


    private final String label;

    StatutEvaluation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
