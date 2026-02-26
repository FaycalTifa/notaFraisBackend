package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.dto.*;
import com.example.notaFraisBackend.entities.entity.*;
import com.example.notaFraisBackend.entities.enume.NiveauMaitrise;
import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.entities.enume.StatutEvaluation;
import com.example.notaFraisBackend.repository.*;
import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ObjectifEvaluationRepository objectifRepository;

    @Autowired
    private ObjectifFuturRepository objectifFuturRepository;

    @Autowired
    private SouhaitFormationRepository formationRepository;

    @Autowired
    private CollaborateurRepository collaborateurRepository;



    // Récupérer l'utilisateur connecté
    private Collaborateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Aucun utilisateur authentifié");
        }

        Object principal = authentication.getPrincipal();

        // Si le principal est un UserDetails (Spring Security)
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User) principal;
            return collaborateurRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + user.getUsername()));
        }

        // Si le principal est une chaîne (email)
        if (principal instanceof String) {
            String email = (String) principal;
            return collaborateurRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        }

        // Si le principal est un CustomUserDetails (selon votre implémentation)
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return collaborateurRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + userDetails.getId()));
        }

        // Log pour déboguer
        System.out.println("Type de principal: " + principal.getClass().getName());
        System.out.println("Principal: " + principal);

        throw new RuntimeException("Type de principal non supporté: " + principal.getClass().getName());
    }
    // Vérifier si l'utilisateur peut évaluer ce collaborateur
    private boolean canEvaluate(Collaborateur evaluateur, Collaborateur collaborateur) {
        if (evaluateur.getRole().toString().equals("ADMIN")) return true;
        if (evaluateur.getRole().toString().equals("DIRECTEUR")) {
            return evaluateur.getDirection() != null &&
                    evaluateur.getDirection().equals(collaborateur.getDirection());
        }
        if (evaluateur.getRole().toString().equals("CHEF_SERVICE")) {
            return evaluateur.getService() != null &&
                    evaluateur.getService().equals(collaborateur.getService());
        }
        if (evaluateur.getRole().toString().equals("CHEF_SECTION")) {
            return evaluateur.getSection() != null &&
                    evaluateur.getSection().equals(collaborateur.getSection());
        }
        return false;
    }

    // Convertir Evaluation en DTO
    private EvaluationDTO convertToDTO(Evaluation evaluation) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(evaluation.getId());
        dto.setAnnee(evaluation.getAnnee());
        dto.setDateCreation(evaluation.getDateCreation());
        dto.setDateEntretien(evaluation.getDateEntretien());
        dto.setDateValidation(evaluation.getDateValidation());
        dto.setStatut(evaluation.getStatut());

        if (evaluation.getCollaborateur() != null) {
            dto.setCollaborateurId(evaluation.getCollaborateur().getId());
            dto.setCollaborateurNom(evaluation.getCollaborateur().getNomComplet());
        }
        if (evaluation.getEvaluateur() != null) {
            dto.setEvaluateurId(evaluation.getEvaluateur().getId());
            dto.setEvaluateurNom(evaluation.getEvaluateur().getNomComplet());
        }

        dto.setFaitsMarquants(evaluation.getFaitsMarquants());

        // Objectifs
        if (evaluation.getObjectifs() != null) {
            dto.setObjectifs(evaluation.getObjectifs().stream()
                    .map(this::convertObjectifToDTO)
                    .collect(Collectors.toList()));
        }
        dto.setNoteGlobaleObjectifs(evaluation.getNoteGlobaleObjectifs() != null ?
                evaluation.getNoteGlobaleObjectifs().doubleValue() : null);

        // Tenue du poste
        dto.setRespectEngagements(evaluation.getRespectEngagements());
        dto.setQualiteMethodesTravail(evaluation.getQualiteMethodesTravail());
        dto.setCapacitesAdaptationOrganisation(evaluation.getCapacitesAdaptationOrganisation());
        dto.setEncadrement(evaluation.getEncadrement());
        dto.setEspritInitiativeInnovation(evaluation.getEspritInitiativeInnovation());
        dto.setRelationPresentation(evaluation.getRelationPresentation());
        dto.setPonctualite(evaluation.getPonctualite());
        dto.setRespectReglementInterieur(evaluation.getRespectReglementInterieur());
        dto.setNoteGlobaleTenuePoste(evaluation.getNoteGlobaleTenuePoste());

        // Maîtrise du poste
        if (evaluation.getNiveauTechnique() != null) {
            dto.setNiveauTechnique(evaluation.getNiveauTechnique().name());
        }
        if (evaluation.getNiveauExperience() != null) {
            dto.setNiveauExperience(evaluation.getNiveauExperience().name());
        }
        if (evaluation.getNiveauEncadrement() != null) {
            dto.setNiveauEncadrement(evaluation.getNiveauEncadrement().name());
        }
        dto.setCommentairesMaitrise(evaluation.getCommentairesMaitrise());

        // Objectifs futurs
        if (evaluation.getObjectifsFuturs() != null) {
            dto.setObjectifsFuturs(evaluation.getObjectifsFuturs().stream()
                    .map(this::convertObjectifFuturToDTO)
                    .collect(Collectors.toList()));
        }

        // Formations
        if (evaluation.getSouhaitsFormations() != null) {
            dto.setSouhaitsFormations(evaluation.getSouhaitsFormations().stream()
                    .map(this::convertFormationToDTO)
                    .collect(Collectors.toList()));
        }

        // Commentaires
        dto.setNoteGlobaleFinale(evaluation.getNoteGlobaleFinale());
        dto.setCommentaireResponsable(evaluation.getCommentaireResponsable());
        dto.setCommentaireCollaborateur(evaluation.getCommentaireCollaborateur());
        dto.setCommentaireN2(evaluation.getCommentaireN2());
        dto.setCommentaireN3(evaluation.getCommentaireN3());
        dto.setSignatureResponsable(evaluation.getSignatureResponsable());
        dto.setSignatureCollaborateur(evaluation.getSignatureCollaborateur());
        dto.setNotesAnterieures(evaluation.getNotesAnterieures());

        return dto;
    }

    private ObjectifEvaluationDTO convertObjectifToDTO(ObjectifEvaluation objectif) {
        ObjectifEvaluationDTO dto = new ObjectifEvaluationDTO();
        dto.setId(objectif.getId());
        dto.setLibelle(objectif.getLibelle());
        dto.setAppreciationCollaborateur(objectif.getAppreciationCollaborateur());
        dto.setAppreciationResponsable(objectif.getAppreciationResponsable());
        dto.setNiveauAtteinte(objectif.getNiveauAtteinte());
        dto.setCotation(objectif.getCotation());
        dto.setTauxAtteinte(objectif.getTauxAtteinte());
        return dto;
    }

    private ObjectifFuturDTO convertObjectifFuturToDTO(ObjectifFutur objectif) {
        ObjectifFuturDTO dto = new ObjectifFuturDTO();
        dto.setId(objectif.getId());
        dto.setLibelle(objectif.getLibelle());
        dto.setPlanAction(objectif.getPlanAction());
        dto.setMoyens(objectif.getMoyens());
        dto.setIndicateursSuivi(objectif.getIndicateursSuivi());
        dto.setType(objectif.getType());
        return dto;
    }

    private SouhaitFormationDTO convertFormationToDTO(SouhaitFormation formation) {
        SouhaitFormationDTO dto = new SouhaitFormationDTO();
        dto.setId(formation.getId());
        dto.setTheme(formation.getTheme());
        dto.setObjectifs(formation.getObjectifs());
        dto.setResultatsAttendus(formation.getResultatsAttendus());
        dto.setDelaisEvaluation(formation.getDelaisEvaluation());
        return dto;
    }

    // Créer une nouvelle évaluation
    @Transactional
    public EvaluationDTO createEvaluation(EvaluationRequestDTO request) {
        Collaborateur currentUser = getCurrentUser();

        Collaborateur collaborateur = collaborateurRepository.findById(request.getCollaborateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur non trouvé"));

        // Vérifier les droits
        if (!canEvaluate(currentUser, collaborateur)) {
            throw new UnauthorizedException("Vous n'avez pas le droit d'évaluer ce collaborateur");
        }

        // Vérifier si une évaluation existe déjà pour cette année
        if (evaluationRepository.existsByCollaborateurIdAndAnnee(collaborateur.getId(), request.getAnnee())) {
            throw new BadRequestException("Une évaluation existe déjà pour ce collaborateur en " + request.getAnnee());
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setAnnee(request.getAnnee());
        evaluation.setDateCreation(LocalDate.now());
        evaluation.setDateEntretien(request.getDateEntretien());
        evaluation.setStatut(StatutEvaluation.BROUILLON);
        evaluation.setCollaborateur(collaborateur);
        evaluation.setEvaluateur(currentUser);

        // Faits marquants
        if (request.getFaitsMarquants() != null) {
            evaluation.setFaitsMarquants(request.getFaitsMarquants());
        }

        // Sauvegarder l'évaluation
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        // Ajouter les objectifs si présents
        if (request.getObjectifs() != null) {
            for (ObjectifEvaluationDTO objDTO : request.getObjectifs()) {
                ObjectifEvaluation objectif = new ObjectifEvaluation();
                objectif.setLibelle(objDTO.getLibelle());
                objectif.setCotation(objDTO.getCotation());
                objectif.setTauxAtteinte(objDTO.getTauxAtteinte());
                objectif.setAppreciationCollaborateur(objDTO.getAppreciationCollaborateur());
                objectif.setAppreciationResponsable(objDTO.getAppreciationResponsable());
                objectif.setEvaluation(savedEvaluation);
                objectifRepository.save(objectif);
                savedEvaluation.getObjectifs().add(objectif);
            }
        }
System.out.println("----------------------savedEvaluation-------------------------------");
System.out.println(savedEvaluation);
System.out.println("-----------------------savedEvaluation------------------------------");
        return convertToDTO(savedEvaluation);
    }

    // Mettre à jour une évaluation
    // Mettre à jour une évaluation
// Mettre à jour une évaluation
/*    @Transactional
    public EvaluationDTO updateEvaluation(Long id, EvaluationRequestDTO request) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();

        // Vérifier les droits (seul l'évaluateur ou admin peut modifier)
        if (!currentUser.getId().equals(evaluation.getEvaluateur().getId()) &&
                !currentUser.getRole().toString().equals("ADMIN")) {
            throw new UnauthorizedException("Vous n'avez pas le droit de modifier cette évaluation");
        }

        // ========== Mise à jour des champs simples ==========
        if (request.getDateEntretien() != null) {
            evaluation.setDateEntretien(request.getDateEntretien());
        }

        if (request.getFaitsMarquants() != null) {
            evaluation.setFaitsMarquants(request.getFaitsMarquants());
        }

        // Tenue du poste
        if (request.getRespectEngagements() != null) {
            evaluation.setRespectEngagements(request.getRespectEngagements());
        }
        if (request.getQualiteMethodesTravail() != null) {
            evaluation.setQualiteMethodesTravail(request.getQualiteMethodesTravail());
        }
        if (request.getCapacitesAdaptationOrganisation() != null) {
            evaluation.setCapacitesAdaptationOrganisation(request.getCapacitesAdaptationOrganisation());
        }
        if (request.getEncadrement() != null) {
            evaluation.setEncadrement(request.getEncadrement());
        }
        if (request.getEspritInitiativeInnovation() != null) {
            evaluation.setEspritInitiativeInnovation(request.getEspritInitiativeInnovation());
        }
        if (request.getRelationPresentation() != null) {
            evaluation.setRelationPresentation(request.getRelationPresentation());
        }
        if (request.getPonctualite() != null) {
            evaluation.setPonctualite(request.getPonctualite());
        }
        if (request.getRespectReglementInterieur() != null) {
            evaluation.setRespectReglementInterieur(request.getRespectReglementInterieur());
        }

        // Maîtrise du poste
        if (request.getNiveauTechnique() != null) {
            evaluation.setNiveauTechnique(NiveauMaitrise.valueOf(request.getNiveauTechnique()));
        }
        if (request.getNiveauExperience() != null) {
            evaluation.setNiveauExperience(NiveauMaitrise.valueOf(request.getNiveauExperience()));
        }
        if (request.getNiveauEncadrement() != null) {
            evaluation.setNiveauEncadrement(NiveauMaitrise.valueOf(request.getNiveauEncadrement()));
        }
        if (request.getCommentairesMaitrise() != null) {
            evaluation.setCommentairesMaitrise(request.getCommentairesMaitrise());
        }

        // Commentaires
        if (request.getCommentaireResponsable() != null) {
            evaluation.setCommentaireResponsable(request.getCommentaireResponsable());
        }
        if (request.getCommentaireCollaborateur() != null) {
            evaluation.setCommentaireCollaborateur(request.getCommentaireCollaborateur());
        }
        if (request.getCommentaireN2() != null) {
            evaluation.setCommentaireN2(request.getCommentaireN2());
        }
        if (request.getCommentaireN3() != null) {
            evaluation.setCommentaireN3(request.getCommentaireN3());
        }

        // ========== GESTION DES OBJECTIFS ==========
        if (request.getObjectifs() != null) {
            // Supprimer les anciens objectifs
            objectifRepository.deleteByEvaluationId(evaluation.getId());

            // Vider la liste existante
            evaluation.getObjectifs().clear();

            // Ajouter les nouveaux objectifs
            for (ObjectifEvaluationDTO objDTO : request.getObjectifs()) {
                if (objDTO.getLibelle() != null && !objDTO.getLibelle().trim().isEmpty()) {
                    ObjectifEvaluation objectif = new ObjectifEvaluation();
                    objectif.setLibelle(objDTO.getLibelle());
                    objectif.setCotation(objDTO.getCotation());
                    objectif.setTauxAtteinte(objDTO.getTauxAtteinte());
                    objectif.setAppreciationCollaborateur(objDTO.getAppreciationCollaborateur());
                    objectif.setAppreciationResponsable(objDTO.getAppreciationResponsable());
                    objectif.setNiveauAtteinte(objDTO.getNiveauAtteinte());
                    objectif.setEvaluation(evaluation);

                    objectifRepository.save(objectif);
                    evaluation.getObjectifs().add(objectif);

                    System.out.println("Objectif ajouté: " + objDTO.getLibelle() + " - Cotation: " + objDTO.getCotation());
                }
            }
        }

        // ========== GESTION DES OBJECTIFS FUTURS ==========
        if (request.getObjectifsFuturs() != null) {
            // Supprimer les anciens objectifs futurs
            objectifFuturRepository.deleteByEvaluationId(evaluation.getId());

            // Vider la liste existante
            evaluation.getObjectifsFuturs().clear();

            // Ajouter les nouveaux objectifs futurs
            for (ObjectifFuturDTO objDTO : request.getObjectifsFuturs()) {
                if (objDTO.getLibelle() != null && !objDTO.getLibelle().trim().isEmpty()) {
                    ObjectifFutur objectif = new ObjectifFutur();
                    objectif.setLibelle(objDTO.getLibelle());
                    objectif.setPlanAction(objDTO.getPlanAction());
                    objectif.setMoyens(objDTO.getMoyens());
                    objectif.setIndicateursSuivi(objDTO.getIndicateursSuivi());
                    objectif.setType(objDTO.getType());
                    objectif.setEvaluation(evaluation);

                    objectifFuturRepository.save(objectif);
                    evaluation.getObjectifsFuturs().add(objectif);

                    System.out.println("Objectif futur ajouté: " + objDTO.getLibelle());
                }
            }
        }

        // ========== GESTION DES FORMATIONS ==========
        if (request.getSouhaitsFormations() != null) {
            // Supprimer les anciennes formations
            formationRepository.deleteByEvaluationId(evaluation.getId());

            // Vider la liste existante
            evaluation.getSouhaitsFormations().clear();

            // Ajouter les nouvelles formations
            for (SouhaitFormationDTO formDTO : request.getSouhaitsFormations()) {
                if (formDTO.getTheme() != null && !formDTO.getTheme().trim().isEmpty()) {
                    SouhaitFormation formation = new SouhaitFormation();
                    formation.setTheme(formDTO.getTheme());
                    formation.setObjectifs(formDTO.getObjectifs());
                    formation.setResultatsAttendus(formDTO.getResultatsAttendus());
                    formation.setDelaisEvaluation(formDTO.getDelaisEvaluation());
                    formation.setEvaluation(evaluation);

                    formationRepository.save(formation);
                    evaluation.getSouhaitsFormations().add(formation);

                    System.out.println("Formation ajoutée: " + formDTO.getTheme());
                }
            }
        }

        // ========== RECALCUL DES NOTES ==========
        evaluation.calculerNotes();

        // ========== LOGS DE VÉRIFICATION ==========
        System.out.println("=== ÉVALUATION MISE À JOUR ===");
        System.out.println("ID: " + evaluation.getId());
        System.out.println("Nombre d'objectifs: " + (evaluation.getObjectifs() != null ? evaluation.getObjectifs().size() : 0));
        System.out.println("Nombre d'objectifs futurs: " + (evaluation.getObjectifsFuturs() != null ? evaluation.getObjectifsFuturs().size() : 0));
        System.out.println("Nombre de formations: " + (evaluation.getSouhaitsFormations() != null ? evaluation.getSouhaitsFormations().size() : 0));
        System.out.println("Note objectifs: " + evaluation.getNoteGlobaleObjectifs());
        System.out.println("Note tenue: " + evaluation.getNoteGlobaleTenuePoste());
        System.out.println("Note finale: " + evaluation.getNoteGlobaleFinale());
        System.out.println("==============================");

        return convertToDTO(evaluationRepository.save(evaluation));
    }*/
    // Mettre à jour une évaluation
    @Transactional
    public EvaluationDTO updateEvaluation(Long id, EvaluationRequestDTO request) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();

        // Vérifier les droits
        if (!currentUser.getId().equals(evaluation.getEvaluateur().getId()) &&
                !currentUser.getRole().toString().equals("ADMIN")) {
            throw new UnauthorizedException("Vous n'avez pas le droit de modifier cette évaluation");
        }

        // ========== Mise à jour des champs ==========
        if (request.getDateEntretien() != null) {
            evaluation.setDateEntretien(request.getDateEntretien());
        }

        if (request.getFaitsMarquants() != null) {
            evaluation.setFaitsMarquants(request.getFaitsMarquants());
        }

        // Tenue du poste
        if (request.getRespectEngagements() != null) {
            evaluation.setRespectEngagements(request.getRespectEngagements());
        }
        if (request.getQualiteMethodesTravail() != null) {
            evaluation.setQualiteMethodesTravail(request.getQualiteMethodesTravail());
        }
        if (request.getCapacitesAdaptationOrganisation() != null) {
            evaluation.setCapacitesAdaptationOrganisation(request.getCapacitesAdaptationOrganisation());
        }
        if (request.getEncadrement() != null) {
            evaluation.setEncadrement(request.getEncadrement());
        }
        if (request.getEspritInitiativeInnovation() != null) {
            evaluation.setEspritInitiativeInnovation(request.getEspritInitiativeInnovation());
        }
        if (request.getRelationPresentation() != null) {
            evaluation.setRelationPresentation(request.getRelationPresentation());
        }
        if (request.getPonctualite() != null) {
            evaluation.setPonctualite(request.getPonctualite());
        }
        if (request.getRespectReglementInterieur() != null) {
            evaluation.setRespectReglementInterieur(request.getRespectReglementInterieur());
        }

        // Maîtrise du poste
        if (request.getNiveauTechnique() != null) {
            evaluation.setNiveauTechnique(NiveauMaitrise.valueOf(request.getNiveauTechnique()));
        }
        if (request.getNiveauExperience() != null) {
            evaluation.setNiveauExperience(NiveauMaitrise.valueOf(request.getNiveauExperience()));
        }
        if (request.getNiveauEncadrement() != null) {
            evaluation.setNiveauEncadrement(NiveauMaitrise.valueOf(request.getNiveauEncadrement()));
        }
        if (request.getCommentairesMaitrise() != null) {
            evaluation.setCommentairesMaitrise(request.getCommentairesMaitrise());
        }

        // Commentaires
        if (request.getCommentaireResponsable() != null) {
            evaluation.setCommentaireResponsable(request.getCommentaireResponsable());
        }
        if (request.getCommentaireCollaborateur() != null) {
            evaluation.setCommentaireCollaborateur(request.getCommentaireCollaborateur());
        }
        if (request.getCommentaireN2() != null) {
            evaluation.setCommentaireN2(request.getCommentaireN2());
        }
        if (request.getCommentaireN3() != null) {
            evaluation.setCommentaireN3(request.getCommentaireN3());
        }

        // ========== GESTION DES OBJECTIFS ==========
        if (request.getObjectifs() != null) {
            objectifRepository.deleteByEvaluationId(evaluation.getId());
            evaluation.getObjectifs().clear();

            for (ObjectifEvaluationDTO objDTO : request.getObjectifs()) {
                if (objDTO.getLibelle() != null && !objDTO.getLibelle().trim().isEmpty()) {
                    ObjectifEvaluation objectif = new ObjectifEvaluation();
                    objectif.setLibelle(objDTO.getLibelle());
                    objectif.setCotation(objDTO.getCotation());
                    objectif.setTauxAtteinte(objDTO.getTauxAtteinte());
                    objectif.setAppreciationCollaborateur(objDTO.getAppreciationCollaborateur());
                    objectif.setAppreciationResponsable(objDTO.getAppreciationResponsable());
                    objectif.setNiveauAtteinte(objDTO.getNiveauAtteinte());
                    objectif.setEvaluation(evaluation);

                    objectifRepository.save(objectif);
                    evaluation.getObjectifs().add(objectif);
                }
            }
        }

        // ========== GESTION DES OBJECTIFS FUTURS ==========
        if (request.getObjectifsFuturs() != null) {
            objectifFuturRepository.deleteByEvaluationId(evaluation.getId());
            evaluation.getObjectifsFuturs().clear();

            for (ObjectifFuturDTO objDTO : request.getObjectifsFuturs()) {
                if (objDTO.getLibelle() != null && !objDTO.getLibelle().trim().isEmpty()) {
                    ObjectifFutur objectif = new ObjectifFutur();
                    objectif.setLibelle(objDTO.getLibelle());
                    objectif.setPlanAction(objDTO.getPlanAction());
                    objectif.setMoyens(objDTO.getMoyens());
                    objectif.setIndicateursSuivi(objDTO.getIndicateursSuivi());
                    objectif.setType(objDTO.getType());
                    objectif.setEvaluation(evaluation);

                    objectifFuturRepository.save(objectif);
                    evaluation.getObjectifsFuturs().add(objectif);
                }
            }
        }

        // ========== GESTION DES FORMATIONS ==========
        if (request.getSouhaitsFormations() != null) {
            formationRepository.deleteByEvaluationId(evaluation.getId());
            evaluation.getSouhaitsFormations().clear();

            for (SouhaitFormationDTO formDTO : request.getSouhaitsFormations()) {
                if (formDTO.getTheme() != null && !formDTO.getTheme().trim().isEmpty()) {
                    SouhaitFormation formation = new SouhaitFormation();
                    formation.setTheme(formDTO.getTheme());
                    formation.setObjectifs(formDTO.getObjectifs());
                    formation.setResultatsAttendus(formDTO.getResultatsAttendus());
                    formation.setDelaisEvaluation(formDTO.getDelaisEvaluation());
                    formation.setEvaluation(evaluation);

                    formationRepository.save(formation);
                    evaluation.getSouhaitsFormations().add(formation);
                }
            }
        }

        // ========== RECALCUL DES NOTES ==========
        evaluation.calculerNotes();

        // ========== VALIDATION AUTOMATIQUE ==========
        // Vérifier si toutes les sections obligatoires sont remplies
        boolean toutesSectionsRemplies = true;

        // Vérifier les objectifs (au moins un objectif avec cotation)
        boolean aDesObjectifs = evaluation.getObjectifs() != null &&
                !evaluation.getObjectifs().isEmpty() &&
                evaluation.getObjectifs().stream().anyMatch(obj -> obj.getCotation() != null);

        // Vérifier la tenue du poste (au moins un critère noté)
        boolean aDesNotesTenue = evaluation.getRespectEngagements() != null ||
                evaluation.getQualiteMethodesTravail() != null ||
                evaluation.getCapacitesAdaptationOrganisation() != null ||
                evaluation.getEncadrement() != null ||
                evaluation.getEspritInitiativeInnovation() != null ||
                evaluation.getRelationPresentation() != null ||
                evaluation.getPonctualite() != null ||
                evaluation.getRespectReglementInterieur() != null;

        // Vérifier les commentaires
        boolean aDesCommentaires = (evaluation.getCommentaireResponsable() != null && !evaluation.getCommentaireResponsable().isEmpty()) ||
                (evaluation.getCommentaireCollaborateur() != null && !evaluation.getCommentaireCollaborateur().isEmpty());

        // Si tout est rempli, valider automatiquement
        if (aDesObjectifs && aDesNotesTenue && aDesCommentaires) {
            evaluation.setStatut(StatutEvaluation.VALIDEE);
            evaluation.setDateValidation(LocalDate.now());
            System.out.println("✅ Évaluation automatiquement validée - Toutes les sections sont remplies");
        } else {
            // Sinon, reste en brouillon
            evaluation.setStatut(StatutEvaluation.BROUILLON);
            System.out.println("📝 Évaluation en brouillon - Sections manquantes:");
            if (!aDesObjectifs) System.out.println("   - Objectifs manquants ou sans cotation");
            if (!aDesNotesTenue) System.out.println("   - Notes de tenue manquantes");
            if (!aDesCommentaires) System.out.println("   - Commentaires manquants");
        }

        // ========== LOGS DE VÉRIFICATION ==========
        System.out.println("=== ÉVALUATION MISE À JOUR ===");
        System.out.println("ID: " + evaluation.getId());
        System.out.println("Statut: " + evaluation.getStatut());
        System.out.println("Objectifs: " + (evaluation.getObjectifs() != null ? evaluation.getObjectifs().size() : 0));
        System.out.println("Notes tenue: " + aDesNotesTenue);
        System.out.println("Commentaires: " + aDesCommentaires);
        System.out.println("Note finale: " + evaluation.getNoteGlobaleFinale());
        System.out.println("==============================");

        return convertToDTO(evaluationRepository.save(evaluation));
    }
    // Ajouter un objectif à une évaluation
    @Transactional
    public ObjectifEvaluationDTO addObjectif(Long evaluationId, ObjectifEvaluationDTO objectifDTO) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        ObjectifEvaluation objectif = new ObjectifEvaluation();
        objectif.setLibelle(objectifDTO.getLibelle());
        objectif.setCotation(objectifDTO.getCotation());
        objectif.setTauxAtteinte(objectifDTO.getTauxAtteinte());
        objectif.setAppreciationCollaborateur(objectifDTO.getAppreciationCollaborateur());
        objectif.setAppreciationResponsable(objectifDTO.getAppreciationResponsable());
        objectif.setEvaluation(evaluation);

        ObjectifEvaluation saved = objectifRepository.save(objectif);

        // Recalculer la note globale des objectifs
        evaluation.calculerNotes();
        evaluationRepository.save(evaluation);

        return convertObjectifToDTO(saved);
    }

    // Ajouter un objectif futur
    @Transactional
    public ObjectifFuturDTO addObjectifFutur(Long evaluationId, ObjectifFuturDTO objectifDTO) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        ObjectifFutur objectif = new ObjectifFutur();
        objectif.setLibelle(objectifDTO.getLibelle());
        objectif.setPlanAction(objectifDTO.getPlanAction());
        objectif.setMoyens(objectifDTO.getMoyens());
        objectif.setIndicateursSuivi(objectifDTO.getIndicateursSuivi());
        objectif.setType(objectifDTO.getType());
        objectif.setEvaluation(evaluation);

        ObjectifFutur saved = objectifFuturRepository.save(objectif);
        return convertObjectifFuturToDTO(saved);
    }

    // Ajouter une formation
    @Transactional
    public SouhaitFormationDTO addFormation(Long evaluationId, SouhaitFormationDTO formationDTO) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        SouhaitFormation formation = new SouhaitFormation();
        formation.setTheme(formationDTO.getTheme());
        formation.setObjectifs(formationDTO.getObjectifs());
        formation.setResultatsAttendus(formationDTO.getResultatsAttendus());
        formation.setDelaisEvaluation(formationDTO.getDelaisEvaluation());
        formation.setEvaluation(evaluation);

        SouhaitFormation saved = formationRepository.save(formation);
        return convertFormationToDTO(saved);
    }

    // Changer le statut de l'évaluation
    @Transactional
    public EvaluationDTO changeStatut(Long id, StatutEvaluation nouveauStatut) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        evaluation.setStatut(nouveauStatut);

        if (nouveauStatut == StatutEvaluation.VALIDEE) {
            evaluation.setDateValidation(LocalDate.now());
        }

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    // Signer l'évaluation
    @Transactional
    public EvaluationDTO signerEvaluation(Long id, String signature, boolean isResponsable) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        if (isResponsable) {
            evaluation.setSignatureResponsable(signature);
        } else {
            evaluation.setSignatureCollaborateur(signature);
        }

        // Si les deux signatures sont présentes, passer en validée
        if (evaluation.getSignatureResponsable() != null && evaluation.getSignatureCollaborateur() != null) {
            evaluation.setStatut(StatutEvaluation.VALIDEE);
            evaluation.setDateValidation(LocalDate.now());
        }

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    // Récupérer toutes les évaluations accessibles
    public List<EvaluationDTO> getAllEvaluations() {
        Collaborateur currentUser = getCurrentUser();
        List<Evaluation> evaluations;

        switch (currentUser.getRole().toString()) {
            case "ADMIN":
                evaluations = evaluationRepository.findAll();
                break;
            case "DIRECTEUR":
                if (currentUser.getDirection() != null) {
                    evaluations = evaluationRepository.findByDirectionId(currentUser.getDirection().getId());
                } else {
                    evaluations = List.of();
                }
                break;
            case "CHEF_SERVICE":
                if (currentUser.getService() != null) {
                    evaluations = evaluationRepository.findByServiceId(currentUser.getService().getId());
                } else {
                    evaluations = List.of();
                }
                break;
            case "CHEF_SECTION":
                if (currentUser.getSection() != null) {
                    evaluations = evaluationRepository.findBySectionId(currentUser.getSection().getId());
                } else {
                    evaluations = List.of();
                }
                break;
            default:
                evaluations = evaluationRepository.findByCollaborateurId(currentUser.getId());
        }

        return evaluations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une évaluation par ID
    public EvaluationDTO getEvaluationById(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));
        return convertToDTO(evaluation);
    }

    // Récupérer les évaluations d'un collaborateur
    public List<EvaluationDTO> getEvaluationsByCollaborateur(Long collaborateurId) {
        return evaluationRepository.findByCollaborateurId(collaborateurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer les évaluations à faire pour l'utilisateur connecté
    public List<EvaluationDTO> getEvaluationsAFaire() {
        Collaborateur currentUser = getCurrentUser();
        List<Collaborateur> collaborateursAEvaluer;

        // Récupérer la liste des collaborateurs que l'utilisateur peut évaluer
        switch (currentUser.getRole().toString()) {
            case "ADMIN":
                collaborateursAEvaluer = collaborateurRepository.findAll();
                break;
            case "DIRECTEUR":
                if (currentUser.getDirection() != null) {
                    collaborateursAEvaluer = collaborateurRepository.findByDirectionId(currentUser.getDirection().getId());
                } else {
                    collaborateursAEvaluer = List.of();
                }
                break;
            case "CHEF_SERVICE":
                if (currentUser.getService() != null) {
                    collaborateursAEvaluer = collaborateurRepository.findByServiceId(currentUser.getService().getId());
                } else {
                    collaborateursAEvaluer = List.of();
                }
                break;
            case "CHEF_SECTION":
                if (currentUser.getSection() != null) {
                    collaborateursAEvaluer = collaborateurRepository.findBySectionId(currentUser.getSection().getId());
                } else {
                    collaborateursAEvaluer = List.of();
                }
                break;
            default:
                collaborateursAEvaluer = List.of();
        }

        // Pour chaque collaborateur, vérifier s'il a une évaluation pour l'année en cours
        int anneeCourante = LocalDate.now().getYear();

        return collaborateursAEvaluer.stream()
                .filter(c -> !evaluationRepository.existsByCollaborateurIdAndAnnee(c.getId(), anneeCourante))
                .map(c -> {
                    EvaluationDTO dto = new EvaluationDTO();
                    dto.setCollaborateurId(c.getId());
                    dto.setCollaborateurNom(c.getNomComplet());
                    return dto;
                })
                .collect(Collectors.toList());
    }



}
