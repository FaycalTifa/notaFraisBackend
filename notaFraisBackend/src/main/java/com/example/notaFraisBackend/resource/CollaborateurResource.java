package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.dto.CollaborateurDTO;
import com.example.notaFraisBackend.dto.CollaborateurRequestDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.enume.Role;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.service.AuthService;
import com.example.notaFraisBackend.service.CollaborateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/collaborateurs")
@CrossOrigin(origins = "*")
public class CollaborateurResource {


    @Autowired
    private CollaborateurService collaborateurService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<List<CollaborateurDTO>> getAllCollaborateurs() {
        return ResponseEntity.ok(collaborateurService.getAllCollaborateurs());
    }

    @GetMapping("/evaluables")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<List<CollaborateurDTO>> getCollaborateursEvaluables() {
        return ResponseEntity.ok(collaborateurService.getCollaborateursEvaluables());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION', 'COLLABORATEUR')")
    public ResponseEntity<CollaborateurDTO> getCollaborateurById(@PathVariable Long id) {
        return ResponseEntity.ok(collaborateurService.getCollaborateurById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollaborateurDTO> createCollaborateur(@Valid @RequestBody CollaborateurRequestDTO request) {
        return new ResponseEntity<>(collaborateurService.createCollaborateur(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR')")
    public ResponseEntity<CollaborateurDTO> updateCollaborateur(
            @PathVariable Long id,
            @Valid @RequestBody CollaborateurRequestDTO request) {
        return ResponseEntity.ok(collaborateurService.updateCollaborateur(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCollaborateur(@PathVariable Long id) {
        collaborateurService.deleteCollaborateur(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR', 'CHEF_SERVICE', 'CHEF_SECTION')")
    public ResponseEntity<List<CollaborateurDTO>> rechercherCollaborateurs(@RequestParam String search) {
        return ResponseEntity.ok(collaborateurService.rechercherCollaborateurs(search));
    }
}
