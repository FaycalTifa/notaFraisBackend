package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.dto.*;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.entity.Evaluation;
import com.example.notaFraisBackend.entities.entity.ObjectifEvaluation;
import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.entities.enume.StatutEvaluation;
import com.example.notaFraisBackend.service.AuthService;
import com.example.notaFraisBackend.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/evaluations")
@CrossOrigin(origins = "*")
public class EvaluationResource {

    @Autowired
    private EvaluationService evaluationService;

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
}
