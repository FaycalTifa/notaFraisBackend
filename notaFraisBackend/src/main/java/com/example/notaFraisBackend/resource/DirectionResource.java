package com.example.notaFraisBackend.resource;

import com.example.notaFraisBackend.dto.ancien.DirectionDTO;
import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.repository.poste.DirectionRepository;
import com.example.notaFraisBackend.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/direction")
public class DirectionResource {

    @Autowired
    private DirectionService directionService;

    @GetMapping
    public ResponseEntity<List<DirectionDTO>> getAllDirections() {
        return ResponseEntity.ok(directionService.getAllDirections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectionDTO> getDirectionById(@PathVariable Long id) {
        return ResponseEntity.ok(directionService.getDirectionById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<DirectionDTO> getDirectionByCode(@PathVariable String code) {
        return ResponseEntity.ok(directionService.getDirectionByCode(code));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DirectionDTO> createDirection(@RequestBody DirectionDTO directionDTO) {
        DirectionDTO createdDirection = directionService.createDirection(directionDTO);
        return new ResponseEntity<>(createdDirection, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DirectionDTO> updateDirection(@PathVariable Long id, @RequestBody DirectionDTO directionDTO) {
        DirectionDTO updatedDirection = directionService.updateDirection(id, directionDTO);
        return ResponseEntity.ok(updatedDirection);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDirection(@PathVariable Long id) {
        directionService.deleteDirection(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{directionId}/directeur/{directeurId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DirectionDTO> assignerDirecteur(@PathVariable Long directionId, @PathVariable Long directeurId) {
        DirectionDTO updatedDirection = directionService.assignerDirecteur(directionId, directeurId);
        return ResponseEntity.ok(updatedDirection);
    }
}
