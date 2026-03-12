package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.dto.*;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.entity.Evaluation;
import com.example.notaFraisBackend.entities.entity.ObjectifEvaluation;
import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.entities.enume.StatutEvaluation;
import com.example.notaFraisBackend.service.AuthService;
import com.example.notaFraisBackend.service.EvaluationService;
import com.example.notaFraisBackend.service.ExportService;
import com.github.dockerjava.api.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "*")
public class EvaluationResource {

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private ExportService exportService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION' , 'COLLABORATEUR')")
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/a-faire")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION', 'CHEF_SECTION')")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsAFaire() {
        return ResponseEntity.ok(evaluationService.getEvaluationsAFaire());
    }

    @GetMapping("/collaborateur/{collaborateurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION' , 'COLLABORATEUR')")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByCollaborateur(@PathVariable Long collaborateurId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByCollaborateur(collaborateurId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION', 'COLLABORATEUR')")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<EvaluationDTO> createEvaluation(@Valid @RequestBody EvaluationRequestDTO request) {
        return new ResponseEntity<>(evaluationService.createEvaluation(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<EvaluationDTO> updateEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationRequestDTO request) {
        return ResponseEntity.ok(evaluationService.updateEvaluation(id, request));
    }

    @PostMapping("/{id}/objectifs")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<ObjectifEvaluationDTO> addObjectif(
            @PathVariable Long id,
            @Valid @RequestBody ObjectifEvaluationDTO objectif) {
        return new ResponseEntity<>(evaluationService.addObjectif(id, objectif), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/objectifs-futurs")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<ObjectifFuturDTO> addObjectifFutur(
            @PathVariable Long id,
            @Valid @RequestBody ObjectifFuturDTO objectif) {
        return new ResponseEntity<>(evaluationService.addObjectifFutur(id, objectif), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/formations")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<SouhaitFormationDTO> addFormation(
            @PathVariable Long id,
            @Valid @RequestBody SouhaitFormationDTO formation) {
        return new ResponseEntity<>(evaluationService.addFormation(id, formation), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<EvaluationDTO> changeStatut(
            @PathVariable Long id,
            @RequestParam StatutEvaluation statut) {
        return ResponseEntity.ok(evaluationService.changeStatut(id, statut));
    }

    // Dans EvaluationResource.java
    @PostMapping("/{id}/signature")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION', 'COLLABORATEUR')")
    public ResponseEntity<EvaluationDTO> signerEvaluation(
            @PathVariable Long id,
            @RequestParam String signature,
            @RequestParam boolean isResponsable) {
        return ResponseEntity.ok(evaluationService.signerEvaluation(id, signature, isResponsable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        // À implémenter si nécessaire
        return ResponseEntity.noContent().build();
    }

    // resource/EvaluationResource.java
// Ajoutez ces méthodes après les méthodes existantes

    @PostMapping("/{id}/soumettre-approbation")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<EvaluationDTO> soumettrePourApprobation(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.soumettrePourApprobation(id));
    }

    @PostMapping("/{id}/approuver")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION', 'COLLABORATEUR')")
    public ResponseEntity<EvaluationDTO> approuverEvaluation(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String commentaire = body != null ? body.get("commentaire") : null;
        return ResponseEntity.ok(evaluationService.approuverEvaluation(id, commentaire));
    }

    @PostMapping("/{id}/refuser")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION', 'COLLABORATEUR')")
    public ResponseEntity<EvaluationDTO> refuserEvaluation(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String motif = body.get("motif");
        if (motif == null || motif.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(evaluationService.refuserEvaluation(id, motif));
    }
    @PostMapping("/{id}/valider-chef-service")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHEF_SERVICE')")
    public ResponseEntity<EvaluationDTO> validerParChefService(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.validerParChefService(id));
    }

    @PostMapping("/{id}/valider-directeur")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR')")
    public ResponseEntity<EvaluationDTO> validerParDirecteur(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.validerParDirecteur(id));
    }


    // =============================================
// ENDPOINT D'ANNULATION
// =============================================
    /**
     * Endpoint pour annuler une évaluation
     */
    @PostMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR')")
    public ResponseEntity<EvaluationDTO> annulerEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody AnnulationRequestDTO request) {

        System.out.println("📝 Requête d'annulation reçue pour l'évaluation ID: " + id);
        System.out.println("   Motif: " + request.getMotif());

        EvaluationDTO result = evaluationService.annulerEvaluation(id, request.getMotif());
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint pour retourner avec motif
     */
    @PostMapping("/{id}/retourner")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE')")
    public ResponseEntity<EvaluationDTO> retournerPourModification(
            @PathVariable Long id,
            @Valid @RequestBody AnnulationRequestDTO request) {

        System.out.println("📝 Requête de retour reçue pour l'évaluation ID: " + id);
        System.out.println("   Motif: " + request.getMotif());

        EvaluationDTO result = evaluationService.retournerPourModification(id, request.getMotif());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/reactiver")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EvaluationDTO> reactiverEvaluation(@PathVariable Long id) {
        System.out.println("📝 Requête de réactivation reçue pour l'évaluation ID: " + id);
        EvaluationDTO result = evaluationService.reactiverEvaluation(id);
        return ResponseEntity.ok(result);
    }


}
