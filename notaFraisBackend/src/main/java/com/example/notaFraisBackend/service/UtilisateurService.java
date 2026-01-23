package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.dto.RegisterDTO;
import com.example.notaFraisBackend.entities.*;
import com.example.notaFraisBackend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private ServiceRepository serviceEntiteRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AgentRepository agentRepository;

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

    public Utilisateur createUtilisateurOK(RegisterDTO dto) {
        // Validation des mots de passe
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        // Vérifier si l'username existe déjà
        if (utilisateurRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(dto.getUsername());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setNom(dto.getNom());
        utilisateur.setPassword(dto.getPassword()); // À hasher en production
        utilisateur.setRole(dto.getRole());
        utilisateur.setDeleted(false);

        // Assigner les relations en fonction du rôle
        if (dto.getDirectionId() != null) {
            Direction direction = directionRepository.findById(dto.getDirectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Direction non trouvée"));
            utilisateur.setDirection(direction);
        }

        if (dto.getServiceId() != null) {
            ServiceEntite service = serviceEntiteRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service non trouvé"));
            utilisateur.setService(service);
        }

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Section non trouvée"));
            utilisateur.setSection(section);
        }

        if (dto.getAgentId() != null) {
            Agent agent = agentRepository.findById(dto.getAgentId())
                    .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé"));
            utilisateur.setAgent(agent);
        }

        return utilisateurRepository.save(utilisateur);
    }


    public Utilisateur createUtilisateur(RegisterDTO dto) {
        // 1. Validation des mots de passe
        validatePassword(dto);

        // 2. Validation de l'username
        validateUsername(dto.getUsername());

        // 3. Validation des champs obligatoires
        validateRequiredFields(dto);

        // 4. Validation des règles métier selon le rôle
        validateRoleConstraints(dto);

        // 5. Validation de la cohérence hiérarchique
        validateHierarchicalConsistency(dto);

        // Création de l'utilisateur
        Utilisateur utilisateur = mapDtoToEntity(dto);
        return utilisateurRepository.save(utilisateur);
    }

    private void validatePassword(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        if (dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }

        // Validation de complexité du mot de passe (optionnel)
        if (!dto.getPassword().matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins une majuscule");
        }

        if (!dto.getPassword().matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins un chiffre");
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("L'username est obligatoire");
        }

        if (username.length() < 3) {
            throw new IllegalArgumentException("L'username doit contenir au moins 3 caractères");
        }

        if (utilisateurRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username déjà utilisé");
        }
    }

    private void validateRequiredFields(RegisterDTO dto) {
        if (dto.getPrenom() == null || dto.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }

        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (dto.getRole() == null || dto.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }

        if (!List.of("DIRECTEUR", "CHEF_SERVICE", "CHEF_SECTION", "AGENT").contains(dto.getRole())) {
            throw new IllegalArgumentException("Rôle invalide. Les rôles valides sont: DIRECTEUR, CHEF_SERVICE, CHEF_SECTION, AGENT");
        }
    }

    private void validateRoleConstraints(RegisterDTO dto) {
        switch (dto.getRole()) {
            case "DIRECTEUR":
                if (dto.getDirectionId() == null) {
                    throw new IllegalArgumentException("Un directeur doit avoir une direction");
                }
                // Un directeur ne devrait pas avoir de service/section/agent
                if (dto.getServiceId() != null || dto.getSectionId() != null || dto.getAgentId() != null) {
                    throw new IllegalArgumentException("Un directeur ne peut pas avoir de service, section ou agent");
                }
                break;

            case "CHEF_SERVICE":
                if (dto.getDirectionId() == null) {
                    throw new IllegalArgumentException("Un chef de service doit avoir une direction");
                }
                if (dto.getServiceId() == null) {
                    throw new IllegalArgumentException("Un chef de service doit avoir un service");
                }
                // Un chef de service ne devrait pas avoir de section/agent
                if (dto.getSectionId() != null || dto.getAgentId() != null) {
                    throw new IllegalArgumentException("Un chef de service ne peut pas avoir de section ou agent");
                }
                break;

            case "CHEF_SECTION":
                if (dto.getDirectionId() == null) {
                    throw new IllegalArgumentException("Un chef de section doit avoir une direction");
                }
                if (dto.getServiceId() == null) {
                    throw new IllegalArgumentException("Un chef de section doit avoir un service");
                }
                if (dto.getSectionId() == null) {
                    throw new IllegalArgumentException("Un chef de section doit avoir une section");
                }
                // Un chef de section ne devrait pas avoir d'agent
                if (dto.getAgentId() != null) {
                    throw new IllegalArgumentException("Un chef de section ne peut pas avoir d'agent");
                }
                break;

            case "AGENT":
                if (dto.getDirectionId() == null) {
                    throw new IllegalArgumentException("Un agent doit avoir une direction");
                }
                if (dto.getServiceId() == null) {
                    throw new IllegalArgumentException("Un agent doit avoir un service");
                }
                if (dto.getSectionId() == null) {
                    throw new IllegalArgumentException("Un agent doit avoir une section");
                }
                if (dto.getAgentId() == null) {
                    throw new IllegalArgumentException("Un agent doit avoir un agent lié");
                }
                break;

            default:
                throw new IllegalArgumentException("Rôle invalide");
        }
    }

    private void validateHierarchicalConsistency(RegisterDTO dto) {
        // Vérifier que le service appartient à la direction
        if (dto.getServiceId() != null && dto.getDirectionId() != null) {
            ServiceEntite service = serviceEntiteRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service non trouvé"));

            if (!service.getDirection().getId().equals(dto.getDirectionId())) {
                throw new IllegalArgumentException("Le service n'appartient pas à la direction sélectionnée");
            }
        }

        // Vérifier que la section appartient au service
        if (dto.getSectionId() != null && dto.getServiceId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Section non trouvée"));

            if (!section.getService().getId().equals(dto.getServiceId())) {
                throw new IllegalArgumentException("La section n'appartient pas au service sélectionné");
            }
        }

        // Vérifier que l'agent appartient à la section
        if (dto.getAgentId() != null && dto.getSectionId() != null) {
            Agent agent = agentRepository.findById(dto.getAgentId())
                    .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé"));

            if (!agent.getSection().getId().equals(dto.getSectionId())) {
                throw new IllegalArgumentException("L'agent n'appartient pas à la section sélectionnée");
            }
        }
    }

    private Utilisateur mapDtoToEntity(RegisterDTO dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(dto.getUsername());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setNom(dto.getNom());
        utilisateur.setPassword(dto.getPassword()); // À hasher en production
        utilisateur.setRole(dto.getRole());
        utilisateur.setDeleted(false);

        // Assigner les relations
        if (dto.getDirectionId() != null) {
            Direction direction = directionRepository.findById(dto.getDirectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Direction non trouvée"));
            utilisateur.setDirection(direction);
        }

        if (dto.getServiceId() != null) {
            ServiceEntite service = serviceEntiteRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service non trouvé"));
            utilisateur.setService(service);
        }

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Section non trouvée"));
            utilisateur.setSection(section);
        }

        if (dto.getAgentId() != null) {
            Agent agent = agentRepository.findById(dto.getAgentId())
                    .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé"));
            utilisateur.setAgent(agent);
        }

        return utilisateur;
    }

    // Méthode pour la mise à jour avec validations
    public Utilisateur updateUtilisateur(Long id, RegisterDTO dto) {
        Utilisateur existingUser = utilisateurRepository.findUtilisateurById(id);

        // Validation des mots de passe (seulement si fournis)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            validatePassword(dto);
            existingUser.setPassword(dto.getPassword());
        }

        validateRequiredFields(dto);
        validateRoleConstraints(dto);
        validateHierarchicalConsistency(dto);

        // Mettre à jour les champs
        existingUser.setUsername(dto.getUsername());
        existingUser.setPrenom(dto.getPrenom());
        existingUser.setNom(dto.getNom());
        existingUser.setRole(dto.getRole());

        // Mettre à jour les relations
        if (dto.getDirectionId() != null) {
            Direction direction = directionRepository.findById(dto.getDirectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Direction non trouvée"));
            existingUser.setDirection(direction);
        } else {
            existingUser.setDirection(null);
        }

        if (dto.getServiceId() != null) {
            ServiceEntite service = serviceEntiteRepository.findById(dto.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service non trouvé"));
            existingUser.setService(service);
        } else {
            existingUser.setService(null);
        }

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Section non trouvée"));
            existingUser.setSection(section);
        } else {
            existingUser.setSection(null);
        }

        if (dto.getAgentId() != null) {
            Agent agent = agentRepository.findById(dto.getAgentId())
                    .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé"));
            existingUser.setAgent(agent);
        } else {
            existingUser.setAgent(null);
        }

        return utilisateurRepository.save(existingUser);
    }

    public Utilisateur save(Utilisateur user) {
        return utilisateurRepository.save(user);
    }

    public Utilisateur createUtilisateur(Utilisateur user) {
        // Ici tu peux ajouter un hash pour le mot de passe
        return utilisateurRepository.save(user);
    }

    public Utilisateur login(String username, String password) {
        // 🔸 Trim + normalisation du nom d’utilisateur
        String cleanUsername = username.trim().toLowerCase();
        String cleanPassword = password.trim();

        System.out.println("====================================");
        System.out.println(cleanUsername);
        System.out.println(cleanPassword);
        System.out.println("====================================");

        return utilisateurRepository.findByUsernameAndPassword(cleanUsername, cleanPassword);
    }

    public List<Utilisateur> findAll(){
        List<Utilisateur> agences = utilisateurRepository.findAllByIsDeletedFalse();
        logger.info("========== List agence en cours dans le service traiter ===============\" + banque");
        System.out.println("=====================    agenceList   ========================");
        System.out.println(agences);
        System.out.println("=====================    agenceList   ========================");
        return agences;
    }


    public Utilisateur update(Long id, Utilisateur utilisateur) {

        Utilisateur exitingUser = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La  agence avec l'ID fourni n'existe pas."));
        // Mettez à jour les propriétés de la banque existante avec les détails de la nouvelle banque
        exitingUser.setNom(utilisateur.getNom()); // et ainsi de suite pour les autres propriétés...
        exitingUser.setPrenom(utilisateur.getPrenom()); // par exemple, si 'nom' est un champ de l'entité Banque

        // Enregistrez la periodiciteRemboursement mise à jour dans la base de données
        return utilisateurRepository.save(exitingUser);
    }

    public Utilisateur delete(Long id, Utilisateur utilisateur) {

        Utilisateur exitingUser = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La agence avec l'ID fourni n'existe pas."));
        // Mettez à jour les propriétés de agence existante avec les détails de la nouvelle agence

        exitingUser.setNom(utilisateur.getNom());  // par exemple, si 'nom' est un champ de l'entité Banque
        //exitingUser.setBanque(utilisateur.getBanque()); // et ainsi de suite pour les autres propriétés...
    //    exitingUser.setDeleted(true); // et ainsi de suite pour les autres propriétés...
        // Enregistrez la periodiciteRemboursement mise à jour dans la base de données
        return utilisateurRepository.save(exitingUser);
    }

}
