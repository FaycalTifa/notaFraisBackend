package com.example.notaFraisBackend.dto;

public class RegisterDTO {
    private String username;
    private String prenom;
    private String nom;
    private String password;
    private String confirmPassword; // Ajout de la confirmation
    private String role;
    private Long directionId;
    private Long serviceId;
    private Long sectionId;
    private Long agentId;

    // Getters et Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getDirectionId() { return directionId; }
    public void setDirectionId(Long directionId) { this.directionId = directionId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }
}
