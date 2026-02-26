package com.example.notaFraisBackend.entities.enume;

public enum NiveauAtteinte {

    EXCELLENT("Excellent", "Objectif Dépassé (>100%)", 10, 10),
    BIEN("Bien", "Atteinte de l'objectif [80...100%)", 8, 9),
    PASSABLE("Passable", "Atteinte partielle [55%...75%)", 6, 7),
    INSUFFISANT("Insuffisant", "Atteinte insuffisante [35...50%)", 4, 5),
    FAIBLE("Faible", "Faible atteinte de l'objectif (≤ 30%)", 0, 3);

    private final String label;
    private final String description;
    private final int noteMin;
    private final int noteMax;

    NiveauAtteinte(String label, String description, int noteMin, int noteMax) {
        this.label = label;
        this.description = description;
        this.noteMin = noteMin;
        this.noteMax = noteMax;
    }

    public String getLabel() { return label; }
    public String getDescription() { return description; }
    public int getNoteMin() { return noteMin; }
    public int getNoteMax() { return noteMax; }

    public static NiveauAtteinte fromNote(int note) {
        for (NiveauAtteinte niveau : values()) {
            if (note >= niveau.noteMin && note <= niveau.noteMax) {
                return niveau;
            }
        }
        return FAIBLE;
    }
}
