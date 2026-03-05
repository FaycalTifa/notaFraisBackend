package com.example.notaFraisBackend.dto;

import java.time.LocalDate;

public class FaitMarquantDTO {
    private String type;
    private String description;
    private LocalDate dateEvenement;

    // Constructeurs
    public FaitMarquantDTO() {}

    public FaitMarquantDTO(String type, String description, LocalDate dateEvenement) {
        this.type = type;
        this.description = description;
        this.dateEvenement = dateEvenement;
    }

    // Getters et Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateEvenement() { return dateEvenement; }
    public void setDateEvenement(LocalDate dateEvenement) { this.dateEvenement = dateEvenement; }
}
