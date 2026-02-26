package com.example.notaFraisBackend.entities.enume;

public enum Role {

    ADMIN("Administrateur", "Accès total à l'application"),
    DIRECTEUR("Directeur", "Accès aux collaborateurs de sa direction"),
    CHEF_SERVICE("Chef de Service", "Accès aux collaborateurs de son service"),
    CHEF_SECTION("Chef de Section", "Accès aux collaborateurs de sa section"),
    COLLABORATEUR("Collaborateur", "Accès à ses propres informations");

    private final String libelle;
    private final String description;

    Role(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
}
