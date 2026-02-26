package com.example.notaFraisBackend.dto;

public class SouhaitFormationDTO {

    private Long id;
    private String theme;
    private String objectifs;
    private String resultatsAttendus;
    private String delaisEvaluation;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getObjectifs() { return objectifs; }
    public void setObjectifs(String objectifs) { this.objectifs = objectifs; }

    public String getResultatsAttendus() { return resultatsAttendus; }
    public void setResultatsAttendus(String resultatsAttendus) { this.resultatsAttendus = resultatsAttendus; }

    public String getDelaisEvaluation() { return delaisEvaluation; }
    public void setDelaisEvaluation(String delaisEvaluation) { this.delaisEvaluation = delaisEvaluation; }
}
