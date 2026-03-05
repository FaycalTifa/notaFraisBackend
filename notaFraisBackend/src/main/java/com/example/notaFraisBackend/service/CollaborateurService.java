package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.dto.CollaborateurDTO;
import com.example.notaFraisBackend.dto.CollaborateurRequestDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.entities.poste.Section;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.repository.poste.DirectionRepository;
import com.example.notaFraisBackend.repository.poste.SectionRepository;
import com.example.notaFraisBackend.repository.poste.ServiceRepository;
import com.example.notaFraisBackend.service.impl.UserDetailsImpl;
import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CollaborateurService {

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ CORRECTION: Récupérer l'utilisateur connecté sans cast problématique
    private Collaborateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Aucun utilisateur authentifié");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            return collaborateurRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec email: " + user.getUsername()));
        } else if (principal instanceof Collaborateur) {
            return (Collaborateur) principal;
        } else {
            throw new RuntimeException("Type de principal non supporté: " + principal.getClass().getName());
        }
    }

    // Récupérer les informations de l'utilisateur connecté
    private UserInfo getCurrentUserInfo() {
        Collaborateur currentUser = getCurrentUser();
        UserInfo info = new UserInfo();
        info.setId(currentUser.getId());
        info.setRole(currentUser.getRole().toString());
        info.setDirectionId(currentUser.getDirection() != null ? currentUser.getDirection().getId() : null);
        info.setServiceId(currentUser.getService() != null ? currentUser.getService().getId() : null);
        info.setSectionId(currentUser.getSection() != null ? currentUser.getSection().getId() : null);
        return info;
    }

    // Classe interne pour les infos utilisateur
    private static class UserInfo {
        private Long id;
        private String role;
        private Long directionId;
        private Long serviceId;
        private Long sectionId;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getDirectionId() { return directionId; }
        public void setDirectionId(Long directionId) { this.directionId = directionId; }
        public Long getServiceId() { return serviceId; }
        public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
        public Long getSectionId() { return sectionId; }
        public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
    }

    // Vérifier les droits d'accès
    private boolean canAccessCollaborateur(Collaborateur collaborateur, UserInfo currentUser) {
        String role = currentUser.getRole();

        if (role.equals("ADMIN")) return true;
        if (role.equals("DIRECTEUR")) {
            return currentUser.getDirectionId() != null &&
                    currentUser.getDirectionId().equals(collaborateur.getDirection() != null ? collaborateur.getDirection().getId() : null);
        }
        if (role.equals("CHEF_SERVICE")) {
            return currentUser.getServiceId() != null &&
                    currentUser.getServiceId().equals(collaborateur.getService() != null ? collaborateur.getService().getId() : null);
        }
        if (role.equals("CHEF_SECTION")) {
            return currentUser.getSectionId() != null &&
                    currentUser.getSectionId().equals(collaborateur.getSection() != null ? collaborateur.getSection().getId() : null);
        }
        return currentUser.getId().equals(collaborateur.getId());
    }

    // Convertir entité en DTO
    private CollaborateurDTO convertToDTO(Collaborateur collaborateur) {
        CollaborateurDTO dto = new CollaborateurDTO();
        dto.setId(collaborateur.getId());
        dto.setNom(collaborateur.getNom());
        dto.setPrenoms(collaborateur.getPrenoms());
        dto.setNomComplet(collaborateur.getNomComplet());
        dto.setMatricule(collaborateur.getMatricule());
        dto.setPosteActuel(collaborateur.getPosteActuel());
        dto.setRole(collaborateur.getRole());
        // ✅ AJOUTER LA SIGNATURE
        dto.setSignature(collaborateur.getSignature());
        dto.setSignatureFilename(collaborateur.getSignatureFilename());
        dto.setSignatureContentType(collaborateur.getSignatureContentType());

        if (collaborateur.getDirection() != null) {
            dto.setDirectionId(collaborateur.getDirection().getId());
            dto.setDirectionNom(collaborateur.getDirection().getNom());
        }
        if (collaborateur.getService() != null) {
            dto.setServiceId(collaborateur.getService().getId());
            dto.setServiceNom(collaborateur.getService().getNom());
        }
        if (collaborateur.getSection() != null) {
            dto.setSectionId(collaborateur.getSection().getId());
            dto.setSectionNom(collaborateur.getSection().getNom());
        }

        return dto;
    }

    // ✅ Lister tous les collaborateurs accessibles - VERSION CORRIGÉE
    @Transactional
    public List<CollaborateurDTO> getAllCollaborateurs() {
        UserInfo currentUser = getCurrentUserInfo();
        List<Collaborateur> collaborateurs;

        switch (currentUser.getRole()) {
            case "ADMIN":
                collaborateurs = collaborateurRepository.findAll();
                break;
            case "DIRECTEUR":
                if (currentUser.getDirectionId() != null) {
                    collaborateurs = collaborateurRepository.findByDirectionId(currentUser.getDirectionId());
                } else {
                    collaborateurs = List.of();
                }
                break;
            case "CHEF_SERVICE":
                if (currentUser.getServiceId() != null) {
                    collaborateurs = collaborateurRepository.findByServiceId(currentUser.getServiceId());
                } else {
                    collaborateurs = List.of();
                }
                break;
            case "CHEF_SECTION":
                if (currentUser.getSectionId() != null) {
                    collaborateurs = collaborateurRepository.findBySectionId(currentUser.getSectionId());
                } else {
                    collaborateurs = List.of();
                }
                break;
            default:
                Collaborateur user = collaborateurRepository.findById(currentUser.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Collaborateur non trouvé"));
                collaborateurs = List.of(user);
        }

        return collaborateurs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un collaborateur par ID
    @Transactional
    public CollaborateurDTO getCollaborateurById(Long id) {
        Collaborateur collaborateur = collaborateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur non trouvé avec id: " + id));

        UserInfo currentUser = getCurrentUserInfo();

        if (!canAccessCollaborateur(collaborateur, currentUser)) {
            throw new com.github.dockerjava.api.exception.UnauthorizedException("Vous n'avez pas les droits pour voir ce collaborateur");
        }

        return convertToDTO(collaborateur);
    }

    // Créer un collaborateur
    @Transactional
    public CollaborateurDTO createCollaborateur(CollaborateurRequestDTO request) {
        // Vérifier unicité
        if (collaborateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un collaborateur avec cet email existe déjà");
        }
        if (collaborateurRepository.existsByMatricule(request.getMatricule())) {
            throw new BadRequestException("Un collaborateur avec ce matricule existe déjà");
        }

        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setNom(request.getNom());
        collaborateur.setPrenoms(request.getPrenoms());
        collaborateur.setMatricule(request.getMatricule());
        collaborateur.setEmail(request.getEmail());
        collaborateur.setTelephone(request.getTelephone());
        collaborateur.setDateEmbauche(request.getDateEmbauche() != null ? request.getDateEmbauche() : LocalDate.now());
        collaborateur.setPosteActuel(request.getPosteActuel());
        collaborateur.setRole(request.getRole());

        // ✅ AJOUTER LA SIGNATURE ICI !
        if (request.getSignature() != null && !request.getSignature().isEmpty()) {
            System.out.println("📝 Sauvegarde de la signature...");
            collaborateur.setSignature(request.getSignature());
            collaborateur.setSignatureFilename(request.getSignatureFilename());
            collaborateur.setSignatureContentType(request.getSignatureContentType());
        } else {
            System.out.println("⚠️ Aucune signature reçue");
        }

        // Mot de passe par défaut = matricule
        String password = request.getPassword() != null ? request.getPassword() : request.getMatricule();
        collaborateur.setPassword(passwordEncoder.encode(password));

        // Affectations
        if (request.getDirectionId() != null) {
            Direction direction = directionRepository.findById(request.getDirectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée"));
            collaborateur.setDirection(direction);
        }
        if (request.getServiceId() != null) {
            ServiceEntite service = serviceRepository.findById(request.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé"));
            collaborateur.setService(service);
        }
        if (request.getSectionId() != null) {
            Section section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée"));
            collaborateur.setSection(section);
        }
        if (request.getResponsableDirectId() != null) {
            Collaborateur responsable = collaborateurRepository.findById(request.getResponsableDirectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Responsable non trouvé"));
            collaborateur.setResponsableDirect(responsable);
            // Le responsable hiérarchique est le responsable du responsable
            collaborateur.setResponsableHierarchique(responsable.getResponsableDirect() != null ?
                    responsable.getResponsableDirect() : responsable);
        }

        collaborateur.updateNomComplet();
        Collaborateur saved = collaborateurRepository.save(collaborateur);
        System.out.println("✅ Collaborateur créé avec signature: " + (saved.getSignature() != null ? "Oui" : "Non"));
        return convertToDTO(saved);
    }

    // Mettre à jour un collaborateur
    @Transactional
    public CollaborateurDTO updateCollaborateur(Long id, CollaborateurRequestDTO request) {
        Collaborateur collaborateur = collaborateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur non trouvé avec id: " + id));

        UserInfo currentUser = getCurrentUserInfo();

        if (!canAccessCollaborateur(collaborateur, currentUser)) {
            throw new com.github.dockerjava.api.exception.UnauthorizedException("Vous n'avez pas les droits pour modifier ce collaborateur");
        }

        // Vérifications d'unicité si les champs ont changé
        if (!collaborateur.getEmail().equals(request.getEmail()) &&
                collaborateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un collaborateur avec cet email existe déjà");
        }
        if (!collaborateur.getMatricule().equals(request.getMatricule()) &&
                collaborateurRepository.existsByMatricule(request.getMatricule())) {
            throw new BadRequestException("Un collaborateur avec ce matricule existe déjà");
        }

        collaborateur.setNom(request.getNom());
        collaborateur.setPrenoms(request.getPrenoms());
        collaborateur.setMatricule(request.getMatricule());
        collaborateur.setEmail(request.getEmail());
        collaborateur.setTelephone(request.getTelephone());
        collaborateur.setDateEmbauche(request.getDateEmbauche() != null ? request.getDateEmbauche() : collaborateur.getDateEmbauche());
        collaborateur.setPosteActuel(request.getPosteActuel());
        collaborateur.setRole(request.getRole());

        // Mot de passe optionnel
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            collaborateur.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        collaborateur.updateNomComplet();
        Collaborateur updated = collaborateurRepository.save(collaborateur);
        return convertToDTO(updated);
    }

    // Désactiver un collaborateur (suppression logique)
    @Transactional
    public void deleteCollaborateur(Long id) {
        Collaborateur collaborateur = collaborateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur non trouvé avec id: " + id));

        UserInfo currentUser = getCurrentUserInfo();

        if (!canAccessCollaborateur(collaborateur, currentUser)) {
            throw new com.github.dockerjava.api.exception.UnauthorizedException("Vous n'avez pas les droits pour supprimer ce collaborateur");
        }

        collaborateur.setActif(false);
        collaborateurRepository.save(collaborateur);
    }

    // Rechercher des collaborateurs
    @Transactional
    public List<CollaborateurDTO> rechercherCollaborateurs(String search) {
        UserInfo currentUser = getCurrentUserInfo();
        List<Collaborateur> collaborateurs = collaborateurRepository.rechercher(search);

        return collaborateurs.stream()
                .filter(collab -> canAccessCollaborateur(collab, currentUser))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional()
    public List<CollaborateurDTO> getCollaborateursEvaluables() {

        UserInfo currentUser = getCurrentUserInfo();

        switch (currentUser.getRole()) {

            case "ADMIN":
                return collaborateurRepository.findAllEvaluablesAdmin();

            case "DIRECTEUR":
                return collaborateurRepository
                        .findEvaluablesByDirection(currentUser.getDirectionId());

            case "CHEF_SERVICE":
                return collaborateurRepository
                        .findEvaluablesByService(currentUser.getServiceId());

            case "CHEF_SECTION":
                return collaborateurRepository
                        .findEvaluablesBySection(currentUser.getSectionId());

            default:
                return List.of();
        }
    }
}
