package com.example.notaFraisBackend.entities.entity;

import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.entities.poste.Section;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Collaborateur")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Collaborateur {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenoms")
    private String prenoms;

    @Column(name = "nom_complet")
    private String nomComplet;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "dateEmbauche")
    private LocalDate dateEmbauche;

    @Column(name = "posteActuel")
    private String posteActuel;

    @Column(name = "experience_cumulee")
    private String experienceCumulee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean actif = true;

    // Relations organisationnelles
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "collaborateurs", "services"})
    private Direction direction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "collaborateurs", "sections", "direction"})
    private ServiceEntite service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "collaborateurs", "service"})
    private Section section;

    // Relations hiérarchiques
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_direct_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "subordonnesDirects", "subordonnesIndirects", "evaluations", "evaluationsRealisees"})
    private Collaborateur responsableDirect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_hierarchique_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "subordonnesDirects", "subordonnesIndirects", "evaluations", "evaluationsRealisees"})
    private Collaborateur responsableHierarchique;

    @OneToMany(mappedBy = "responsableDirect", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Collaborateur> subordonnesDirects = new ArrayList<>();

    @OneToMany(mappedBy = "responsableHierarchique", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Collaborateur> subordonnesIndirects = new ArrayList<>();

    // Relations d'évaluation
    @OneToMany(mappedBy = "collaborateur", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "evaluateur", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Evaluation> evaluationsRealisees = new ArrayList<>();

    // Constructeurs
    public Collaborateur() {}

    public Collaborateur(String nom, String prenoms, String matricule, String email, String password, Role role) {
        this.nom = nom;
        this.prenoms = prenoms;
        this.matricule = matricule;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nomComplet = prenoms + " " + nom;
    }

    @PrePersist
    @PreUpdate
    public void updateNomComplet() {
        this.nomComplet = this.prenoms + " " + this.nom;
        if (this.dateEmbauche != null) {
            long mois = ChronoUnit.MONTHS.between(this.dateEmbauche, LocalDate.now());
            long annees = mois / 12;
            long moisRestants = mois % 12;
            this.experienceCumulee = annees + " an(s), " + moisRestants + " mois";
        }
    }

    // Méthodes utilitaires
    public boolean peutEvaluer(Collaborateur collab) {
        if (this.role == Role.ADMIN) return true;
        if (this.role == Role.DIRECTEUR) {
            return this.direction != null && this.direction.equals(collab.getDirection());
        }
        if (this.role == Role.CHEF_SERVICE) {
            return this.service != null && this.service.equals(collab.getService());
        }
        if (this.role == Role.CHEF_SECTION) {
            return this.section != null && this.section.equals(collab.getSection());
        }
        return false;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenoms() { return prenoms; }
    public void setPrenoms(String prenoms) { this.prenoms = prenoms; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getPosteActuel() { return posteActuel; }
    public void setPosteActuel(String posteActuel) { this.posteActuel = posteActuel; }

    public String getExperienceCumulee() { return experienceCumulee; }
    public void setExperienceCumulee(String experienceCumulee) { this.experienceCumulee = experienceCumulee; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public ServiceEntite getService() { return service; }
    public void setService(ServiceEntite service) { this.service = service; }

    public Section getSection() { return section; }
    public void setSection(Section section) { this.section = section; }

    public Collaborateur getResponsableDirect() { return responsableDirect; }
    public void setResponsableDirect(Collaborateur responsableDirect) { this.responsableDirect = responsableDirect; }

    public Collaborateur getResponsableHierarchique() { return responsableHierarchique; }
    public void setResponsableHierarchique(Collaborateur responsableHierarchique) { this.responsableHierarchique = responsableHierarchique; }

    public List<Collaborateur> getSubordonnesDirects() { return subordonnesDirects; }
    public void setSubordonnesDirects(List<Collaborateur> subordonnesDirects) { this.subordonnesDirects = subordonnesDirects; }

    public List<Collaborateur> getSubordonnesIndirects() { return subordonnesIndirects; }
    public void setSubordonnesIndirects(List<Collaborateur> subordonnesIndirects) { this.subordonnesIndirects = subordonnesIndirects; }

    public List<Evaluation> getEvaluations() { return evaluations; }
    public void setEvaluations(List<Evaluation> evaluations) { this.evaluations = evaluations; }

    public List<Evaluation> getEvaluationsRealisees() { return evaluationsRealisees; }
    public void setEvaluationsRealisees(List<Evaluation> evaluationsRealisees) { this.evaluationsRealisees = evaluationsRealisees; }
}
