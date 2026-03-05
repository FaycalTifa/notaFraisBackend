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

    // =============================================
    // MÉTHODES PRIVÉES
    // =============================================
    private Collaborateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Aucun utilisateur authentifié");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User) principal;
            return collaborateurRepository.findByEmail(user.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + user.getUsername()));
        } else if (principal instanceof String) {
            String email = (String) principal;
            return collaborateurRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        } else if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return collaborateurRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id: " + userDetails.getId()));
        }

        throw new RuntimeException("Type de principal non supporté: " + principal.getClass().getName());
    }

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

    // =============================================
    // CONVERSION EN DTO
    // =============================================
    private EvaluationDTO convertToDTO(Evaluation evaluation) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(evaluation.getId());
        dto.setAnnee(evaluation.getAnnee());
        dto.setDateCreation(evaluation.getDateCreation());
        dto.setDateEntretien(evaluation.getDateEntretien());
        dto.setDateValidation(evaluation.getDateValidation());
        dto.setStatut(evaluation.getStatut());
        dto.setDateEntretien(evaluation.getDateEntretien());

        // Collaborateur
        if (evaluation.getCollaborateur() != null) {
            CollaborateurDTO collabDTO = new CollaborateurDTO();
            collabDTO.setId(evaluation.getCollaborateur().getId());
            collabDTO.setNom(evaluation.getCollaborateur().getNom());
            collabDTO.setPrenoms(evaluation.getCollaborateur().getPrenoms());
            collabDTO.setNomComplet(evaluation.getCollaborateur().getNomComplet());
            collabDTO.setMatricule(evaluation.getCollaborateur().getMatricule());
            collabDTO.setPosteActuel(evaluation.getCollaborateur().getPosteActuel());
            collabDTO.setRole(evaluation.getCollaborateur().getRole());

            if (evaluation.getCollaborateur().getDirection() != null) {
                collabDTO.setDirectionId(evaluation.getCollaborateur().getDirection().getId());
                collabDTO.setDirectionNom(evaluation.getCollaborateur().getDirection().getNom());
            }
            if (evaluation.getCollaborateur().getService() != null) {
                collabDTO.setServiceId(evaluation.getCollaborateur().getService().getId());
                collabDTO.setServiceNom(evaluation.getCollaborateur().getService().getNom());
            }
            if (evaluation.getCollaborateur().getSection() != null) {
                collabDTO.setSectionId(evaluation.getCollaborateur().getSection().getId());
                collabDTO.setSectionNom(evaluation.getCollaborateur().getSection().getNom());
            }

            dto.setCollaborateur(collabDTO);
            dto.setCollaborateurId(evaluation.getCollaborateur().getId());
            dto.setCollaborateurNom(evaluation.getCollaborateur().getNomComplet());
        }

        // Évaluateur
        if (evaluation.getEvaluateur() != null) {
            CollaborateurDTO evalDTO = new CollaborateurDTO();
            evalDTO.setId(evaluation.getEvaluateur().getId());
            evalDTO.setNom(evaluation.getEvaluateur().getNom());
            evalDTO.setPrenoms(evaluation.getEvaluateur().getPrenoms());
            evalDTO.setNomComplet(evaluation.getEvaluateur().getNomComplet());
            evalDTO.setMatricule(evaluation.getEvaluateur().getMatricule());
            evalDTO.setPosteActuel(evaluation.getEvaluateur().getPosteActuel());
            evalDTO.setRole(evaluation.getEvaluateur().getRole());

            if (evaluation.getEvaluateur().getDirection() != null) {
                evalDTO.setDirectionId(evaluation.getEvaluateur().getDirection().getId());
                evalDTO.setDirectionNom(evaluation.getEvaluateur().getDirection().getNom());
            }
            if (evaluation.getEvaluateur().getService() != null) {
                evalDTO.setServiceId(evaluation.getEvaluateur().getService().getId());
                evalDTO.setServiceNom(evaluation.getEvaluateur().getService().getNom());
            }
            if (evaluation.getEvaluateur().getSection() != null) {
                evalDTO.setSectionId(evaluation.getEvaluateur().getSection().getId());
                evalDTO.setSectionNom(evaluation.getEvaluateur().getSection().getNom());
            }

            dto.setEvaluateur(evalDTO);
            dto.setEvaluateurId(evaluation.getEvaluateur().getId());
            dto.setEvaluateurNom(evaluation.getEvaluateur().getNomComplet());
        }



        // Objectifs
        if (evaluation.getObjectifs() != null) {
            dto.setObjectifs(evaluation.getObjectifs().stream()
                    .map(this::convertObjectifToDTO)
                    .collect(Collectors.toList()));
        }
        dto.setNoteGlobaleObjectifs(evaluation.getNoteGlobaleObjectifs());

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

if (evaluation.getFaitsMarquants() != null) {
            dto.setFaitsMarquants(evaluation.getFaitsMarquants().stream()
                    .map(this::convertFaitMarquantToDTO)
                    .collect(Collectors.toList()));
        }

        // Commentaires et notes
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


    private FaitMarquantDTO convertFaitMarquantToDTO(FaitMarquant fait) {
        FaitMarquantDTO dto = new FaitMarquantDTO();
        dto.setType(fait.getType());
        dto.setDescription(fait.getDescription());
        dto.setDateEvenement(fait.getDateEvenement());
        return dto;
    }

    private FaitMarquant convertDTOToFaitMarquant(FaitMarquantDTO dto, Evaluation evaluation) {
        FaitMarquant fait = new FaitMarquant();
        fait.setType(dto.getType());
        fait.setDescription(dto.getDescription());
        fait.setDateEvenement(dto.getDateEvenement());
        fait.setEvaluation(evaluation);
        return fait;
    }
    // =============================================
    // CRÉATION
    // =============================================
    @Transactional
    public EvaluationDTO createEvaluation(EvaluationRequestDTO request) {
        Collaborateur currentUser = getCurrentUser();
        Collaborateur collaborateur = collaborateurRepository.findById(request.getCollaborateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur non trouvé"));

        if (!canEvaluate(currentUser, collaborateur)) {
            throw new UnauthorizedException("Vous n'avez pas le droit d'évaluer ce collaborateur");
        }

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

        // Gestion des faits marquants
        if (request.getFaitsMarquants() != null) {
            // Supprimer les anciens
            evaluation.getFaitsMarquants().clear();

            // Ajouter les nouveaux
            for (FaitMarquantDTO fmDTO : request.getFaitsMarquants()) {
                FaitMarquant fait = convertDTOToFaitMarquant(fmDTO, evaluation);
                evaluation.getFaitsMarquants().add(fait);
            }
        }
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        // Ajouter les objectifs
        if (request.getObjectifs() != null) {
            for (ObjectifEvaluationDTO objDTO : request.getObjectifs()) {
                if (objDTO.getLibelle() != null && !objDTO.getLibelle().trim().isEmpty()) {
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
        }

        return convertToDTO(savedEvaluation);
    }

    // =============================================
    // MISE À JOUR COMPLÈTE ET CORRIGÉE
    // =============================================
    @Transactional
    public EvaluationDTO updateEvaluation(Long id, EvaluationRequestDTO request) {
        // LOGS DE DÉBOGAGE
        System.out.println("========== UPDATE EVALUATION ==========");
        System.out.println("ID: " + id);
        System.out.println("Données reçues:");
        System.out.println("  - Date entretien: " + request.getDateEntretien());
        System.out.println("  - Respect engagements: " + request.getRespectEngagements());
        System.out.println("  - Qualité méthodes: " + request.getQualiteMethodesTravail());

        System.out.println("========== UPDATE EVALUATION ==========");
        System.out.println("ID: " + id);

        // Log de la tenue du poste
        System.out.println("--- Tenue du poste ---");
        System.out.println("respectEngagements: " + request.getRespectEngagements());
        System.out.println("qualiteMethodesTravail: " + request.getQualiteMethodesTravail());
        System.out.println("relationPresentation: " + request.getRelationPresentation());

        // Log de la maîtrise
        System.out.println("--- Maîtrise du poste ---");
        System.out.println("niveauTechnique: " + request.getNiveauTechnique());
        System.out.println("niveauExperience: " + request.getNiveauExperience());

        if (request.getObjectifs() != null) {
            System.out.println("  - Objectifs reçus: " + request.getObjectifs().size());
            for (ObjectifEvaluationDTO obj : request.getObjectifs()) {
                System.out.println("    * " + obj.getLibelle() + " (cotation: " + obj.getCotation() + ")");
            }
        } else {
            System.out.println("  - Aucun objectif reçu");
        }

        // Récupérer l'évaluation existante
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée avec l'id: " + id));

        // Récupérer l'utilisateur connecté
        Collaborateur currentUser = getCurrentUser();

        // Vérifier les droits de modification
        if (!currentUser.getId().equals(evaluation.getEvaluateur().getId()) &&
                !currentUser.getRole().toString().equals("ADMIN")) {
            throw new UnauthorizedException("Vous n'avez pas le droit de modifier cette évaluation");
        }

        // ========== MISE À JOUR DES CHAMPS SIMPLES ==========
        if (request.getDateEntretien() != null) {
            evaluation.setDateEntretien(request.getDateEntretien());
        }

        // Gestion des faits marquants
        if (request.getFaitsMarquants() != null) {
            // Supprimer les anciens
            evaluation.getFaitsMarquants().clear();

            // Ajouter les nouveaux
            for (FaitMarquantDTO fmDTO : request.getFaitsMarquants()) {
                FaitMarquant fait = convertDTOToFaitMarquant(fmDTO, evaluation);
                evaluation.getFaitsMarquants().add(fait);
            }
        }

        // Tenue du poste
        if (request.getRespectEngagements() != null)
            evaluation.setRespectEngagements(request.getRespectEngagements());
        if (request.getQualiteMethodesTravail() != null)
            evaluation.setQualiteMethodesTravail(request.getQualiteMethodesTravail());
        if (request.getCapacitesAdaptationOrganisation() != null)
            evaluation.setCapacitesAdaptationOrganisation(request.getCapacitesAdaptationOrganisation());
        if (request.getEncadrement() != null)
            evaluation.setEncadrement(request.getEncadrement());
        if (request.getEspritInitiativeInnovation() != null)
            evaluation.setEspritInitiativeInnovation(request.getEspritInitiativeInnovation());
        if (request.getRelationPresentation() != null)
            evaluation.setRelationPresentation(request.getRelationPresentation());
        if (request.getPonctualite() != null)
            evaluation.setPonctualite(request.getPonctualite());
        if (request.getRespectReglementInterieur() != null)
            evaluation.setRespectReglementInterieur(request.getRespectReglementInterieur());

        // Maîtrise du poste
        if (request.getNiveauTechnique() != null)
            evaluation.setNiveauTechnique(NiveauMaitrise.valueOf(request.getNiveauTechnique()));
        if (request.getNiveauExperience() != null)
            evaluation.setNiveauExperience(NiveauMaitrise.valueOf(request.getNiveauExperience()));
        if (request.getNiveauEncadrement() != null)
            evaluation.setNiveauEncadrement(NiveauMaitrise.valueOf(request.getNiveauEncadrement()));
        if (request.getCommentairesMaitrise() != null)
            evaluation.setCommentairesMaitrise(request.getCommentairesMaitrise());

        // Commentaires
        if (request.getCommentaireResponsable() != null)
            evaluation.setCommentaireResponsable(request.getCommentaireResponsable());
        if (request.getCommentaireCollaborateur() != null)
            evaluation.setCommentaireCollaborateur(request.getCommentaireCollaborateur());
        if (request.getCommentaireN2() != null)
            evaluation.setCommentaireN2(request.getCommentaireN2());
        if (request.getCommentaireN3() != null)
            evaluation.setCommentaireN3(request.getCommentaireN3());

        // ========== GESTION DES OBJECTIFS ==========
        if (request.getObjectifs() != null) {

            // Supprimer les anciens objectifs
            objectifRepository.deleteByEvaluationId(evaluation.getId());
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

                    ObjectifEvaluation saved = objectifRepository.save(objectif);
                    evaluation.getObjectifs().add(saved);
                    System.out.println("✅ Objectif sauvegardé: " + saved.getLibelle());
                }
            }
        }

        // ========== GESTION DES OBJECTIFS FUTURS ==========
        if (request.getObjectifsFuturs() != null) {
            objectifFuturRepository.deleteByEvaluationId(evaluation.getId());
            System.out.println("--- Objectifs futurs (" + request.getObjectifsFuturs().size() + ") ---");
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

                    ObjectifFutur saved = objectifFuturRepository.save(objectif);
                    evaluation.getObjectifsFuturs().add(saved);
                }
            }
        }

        // ========== GESTION DES FORMATIONS ==========
        if (request.getSouhaitsFormations() != null) {
            formationRepository.deleteByEvaluationId(evaluation.getId());
            System.out.println("--- Formations (" + request.getSouhaitsFormations().size() + ") ---");
            evaluation.getSouhaitsFormations().clear();

            for (SouhaitFormationDTO formDTO : request.getSouhaitsFormations()) {
                if (formDTO.getTheme() != null && !formDTO.getTheme().trim().isEmpty()) {
                    SouhaitFormation formation = new SouhaitFormation();
                    formation.setTheme(formDTO.getTheme());
                    formation.setObjectifs(formDTO.getObjectifs());
                    formation.setResultatsAttendus(formDTO.getResultatsAttendus());
                    formation.setDelaisEvaluation(formDTO.getDelaisEvaluation());
                    formation.setEvaluation(evaluation);

                    SouhaitFormation saved = formationRepository.save(formation);
                    evaluation.getSouhaitsFormations().add(saved);
                }
            }
        }

        // ========== RECALCUL DES NOTES ==========
        evaluation.calculerNotes();

        // ========== SAUVEGARDE FINALE ==========
        Evaluation updated = evaluationRepository.save(evaluation);
        System.out.println("✅ Évaluation mise à jour avec succès");
        System.out.println("  - Objectifs: " + updated.getObjectifs().size());
        System.out.println("  - Respect engagements: " + updated.getRespectEngagements());
        System.out.println("========== FIN UPDATE ==========");

        return convertToDTO(updated);
    }

    // =============================================
    // AUTRES MÉTHODES (inchangées)
    // =============================================
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
        evaluation.calculerNotes();
        evaluationRepository.save(evaluation);

        return convertObjectifToDTO(saved);
    }

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

    public EvaluationDTO changeStatut(Long id, StatutEvaluation nouveauStatut) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        evaluation.setStatut(nouveauStatut);
        if (nouveauStatut == StatutEvaluation.VALIDEE) {
            evaluation.setDateValidation(LocalDate.now());
        }

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    public EvaluationDTO signerEvaluation(Long id, String signature, boolean isResponsable) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        if (isResponsable) {
            evaluation.setSignatureResponsable(signature);
        } else {
            evaluation.setSignatureCollaborateur(signature);
        }

        if (evaluation.getSignatureResponsable() != null && evaluation.getSignatureCollaborateur() != null) {
            evaluation.setStatut(StatutEvaluation.VALIDEE);
            evaluation.setDateValidation(LocalDate.now());
        }

        return convertToDTO(evaluationRepository.save(evaluation));
    }

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

    public EvaluationDTO getEvaluationById(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));
        return convertToDTO(evaluation);
    }

    public List<EvaluationDTO> getEvaluationsByCollaborateur(Long collaborateurId) {
        return evaluationRepository.findByCollaborateurId(collaborateurId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EvaluationDTO> getEvaluationsAFaire() {
        Collaborateur currentUser = getCurrentUser();
        List<Collaborateur> collaborateursAEvaluer;

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

    // =============================================
    // MÉTHODES DE WORKFLOW
    // =============================================
    public EvaluationDTO soumettrePourApprobation(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();

        if (!currentUser.getId().equals(evaluation.getEvaluateur().getId())) {
            throw new UnauthorizedException("Seul l'évaluateur peut soumettre l'évaluation");
        }

        evaluation.setStatut(StatutEvaluation.A_APPROUVER);
        return convertToDTO(evaluationRepository.save(evaluation));
    }

    public EvaluationDTO approuverEvaluation(Long id, String commentaire) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();
        Collaborateur collaborateur = evaluation.getCollaborateur();

        if (!currentUser.getId().equals(collaborateur.getId())) {
            throw new UnauthorizedException("Seul le collaborateur évalué peut approuver l'évaluation");
        }

        evaluation.setCommentaireCollaborateur(commentaire);
        Collaborateur evaluateur = evaluation.getEvaluateur();

        if (evaluateur.getRole() == Role.DIRECTEUR) {
            evaluation.setStatut(StatutEvaluation.VALIDEE);
            evaluation.setDateValidation(LocalDate.now());
        } else if (evaluateur.getRole() == Role.CHEF_SERVICE) {
            evaluation.setStatut(StatutEvaluation.A_VALIDER_DIRECTEUR);
        } else if (evaluateur.getRole() == Role.CHEF_SECTION) {
            evaluation.setStatut(StatutEvaluation.A_VALIDER_SERVICE);
        } else {
            evaluation.setStatut(StatutEvaluation.APPROUVEE);
        }

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    public EvaluationDTO refuserEvaluation(Long id, String motif) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();
        Collaborateur collaborateur = evaluation.getCollaborateur();

        if (!currentUser.getId().equals(collaborateur.getId())) {
            throw new UnauthorizedException("Seul le collaborateur évalué peut refuser l'évaluation");
        }

        evaluation.setCommentaireCollaborateur(motif);
        evaluation.setStatut(StatutEvaluation.BROUILLON);

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    public EvaluationDTO validerParChefService(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();
        Collaborateur evaluateur = evaluation.getEvaluateur();

        boolean estLeChefService = currentUser.getRole() == Role.CHEF_SERVICE &&
                currentUser.getService() != null &&
                evaluateur.getService() != null &&
                currentUser.getService().getId().equals(evaluateur.getService().getId());

        if (!estLeChefService && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seul le chef de service de l'évaluateur peut valider cette évaluation");
        }

        evaluation.setStatut(StatutEvaluation.A_VALIDER_DIRECTEUR);
        evaluation.setCommentaireN2("Validé par le chef de service le " + LocalDate.now());

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    public EvaluationDTO validerParDirecteur(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();
        Collaborateur evaluateur = evaluation.getEvaluateur();

        boolean estLeDirecteur = currentUser.getRole() == Role.DIRECTEUR &&
                currentUser.getDirection() != null &&
                evaluateur.getDirection() != null &&
                currentUser.getDirection().getId().equals(evaluateur.getDirection().getId());

        if (!estLeDirecteur && currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Seul le directeur de l'évaluateur peut valider cette évaluation");
        }

        evaluation.setStatut(StatutEvaluation.VALIDEE);
        evaluation.setDateValidation(LocalDate.now());
        evaluation.setCommentaireN3("Validé par le directeur le " + LocalDate.now());

        return convertToDTO(evaluationRepository.save(evaluation));
    }

    public EvaluationDTO retournerPourModification(Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation non trouvée"));

        Collaborateur currentUser = getCurrentUser();

        boolean aLesDroits = currentUser.getRole() == Role.ADMIN ||
                currentUser.getRole() == Role.DIRECTEUR ||
                (currentUser.getRole() == Role.CHEF_SERVICE &&
                        evaluation.getEvaluateur().getService() != null &&
                        currentUser.getService() != null &&
                        currentUser.getService().getId().equals(evaluation.getEvaluateur().getService().getId()));

        if (!aLesDroits) {
            throw new UnauthorizedException("Vous n'avez pas les droits pour retourner cette évaluation");
        }

        evaluation.setStatut(StatutEvaluation.BROUILLON);

        return convertToDTO(evaluationRepository.save(evaluation));
    }
}
