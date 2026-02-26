package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.dto.ancien.DirectionDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.repository.poste.DirectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DirectionService {


    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    // Convertir Entity en DTO
    private DirectionDTO convertToDTO(Direction direction) {
        DirectionDTO dto = new DirectionDTO();
        dto.setId(direction.getId());
        dto.setCode(direction.getCode());
        dto.setNom(direction.getNom());
        dto.setDescription(direction.getDescription());

        if (direction.getDirecteur() != null) {
            dto.setDirecteurId(direction.getDirecteur().getId());
            dto.setDirecteurNom(direction.getDirecteur().getNomComplet());
        }

        dto.setNombreServices(direction.getServices() != null ? direction.getServices().size() : 0);
        dto.setNombreCollaborateurs(direction.getCollaborateurs() != null ? direction.getCollaborateurs().size() : 0);

        return dto;
    }

    // Récupérer toutes les directions
    @Transactional(readOnly = true)
    public List<DirectionDTO> getAllDirections() {
        return directionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une direction par son ID
    @Transactional(readOnly = true)
    public DirectionDTO getDirectionById(Long id) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec l'id: " + id));
        return convertToDTO(direction);
    }

    // Récupérer une direction par son code
    @Transactional(readOnly = true)
    public DirectionDTO getDirectionByCode(String code) {
        Direction direction = directionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec le code: " + code));
        return convertToDTO(direction);
    }

    // Créer une nouvelle direction
    @Transactional
    public DirectionDTO createDirection(DirectionDTO directionDTO) {
        if (directionRepository.existsByCode(directionDTO.getCode())) {
            throw new RuntimeException("Une direction avec le code " + directionDTO.getCode() + " existe déjà");
        }

        Direction direction = new Direction();
        direction.setCode(directionDTO.getCode());
        direction.setNom(directionDTO.getNom());
        direction.setDescription(directionDTO.getDescription());

        if (directionDTO.getDirecteurId() != null) {
            Collaborateur directeur = collaborateurRepository.findById(directionDTO.getDirecteurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé avec l'id: " + directionDTO.getDirecteurId()));
            direction.setDirecteur(directeur);
        }

        Direction savedDirection = directionRepository.save(direction);
        return convertToDTO(savedDirection);
    }

    // Mettre à jour une direction
    @Transactional
    public DirectionDTO updateDirection(Long id, DirectionDTO directionDTO) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec l'id: " + id));

        direction.setNom(directionDTO.getNom());
        direction.setDescription(directionDTO.getDescription());

        if (directionDTO.getDirecteurId() != null) {
            Collaborateur directeur = collaborateurRepository.findById(directionDTO.getDirecteurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé avec l'id: " + directionDTO.getDirecteurId()));
            direction.setDirecteur(directeur);
        } else {
            direction.setDirecteur(null);
        }

        Direction updatedDirection = directionRepository.save(direction);
        return convertToDTO(updatedDirection);
    }

    // Supprimer une direction
    @Transactional
    public void deleteDirection(Long id) {
        Direction direction = directionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec l'id: " + id));

        if (direction.getServices() != null && !direction.getServices().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une direction qui a des services associés");
        }

        directionRepository.delete(direction);
    }

    // Assigner un directeur
    @Transactional
    public DirectionDTO assignerDirecteur(Long directionId, Long directeurId) {
        Direction direction = directionRepository.findById(directionId)
                .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec l'id: " + directionId));

        Collaborateur directeur = collaborateurRepository.findById(directeurId)
                .orElseThrow(() -> new ResourceNotFoundException("Directeur non trouvé avec l'id: " + directeurId));

        direction.setDirecteur(directeur);
        Direction updatedDirection = directionRepository.save(direction);
        return convertToDTO(updatedDirection);
    }
}
