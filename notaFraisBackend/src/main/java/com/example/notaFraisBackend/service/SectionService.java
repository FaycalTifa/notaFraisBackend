package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.dto.ancien.SectionDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.poste.Section;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.repository.poste.SectionRepository;
import com.example.notaFraisBackend.repository.poste.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    // Convertir Entity en DTO
    private SectionDTO convertToDTO(Section section) {
        SectionDTO dto = new SectionDTO();
        dto.setId(section.getId());
        dto.setCode(section.getCode());
        dto.setNom(section.getNom());
        dto.setDescription(section.getDescription());

        if (section.getService() != null) {
            dto.setServiceId(section.getService().getId());
            dto.setServiceNom(section.getService().getNom());
        }

        if (section.getChefSection() != null) {
            dto.setChefSectionId(section.getChefSection().getId());
            dto.setChefSectionNom(section.getChefSection().getNomComplet());
        }

        dto.setNombreCollaborateurs(section.getCollaborateurs() != null ? section.getCollaborateurs().size() : 0);

        return dto;
    }

    // Récupérer toutes les sections
    @Transactional(readOnly = true)
    public List<SectionDTO> getAllSections() {
        return sectionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une section par son ID
    @Transactional(readOnly = true)
    public SectionDTO getSectionById(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec l'id: " + id));
        return convertToDTO(section);
    }

    // Récupérer les sections par service
    @Transactional(readOnly = true)
    public List<SectionDTO> getSectionsByService(Long serviceId) {
        return sectionRepository.findByServiceId(serviceId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Créer une nouvelle section
    @Transactional
    public SectionDTO createSection(SectionDTO sectionDTO) {
        if (sectionRepository.existsByCode(sectionDTO.getCode())) {
            throw new RuntimeException("Une section avec le code " + sectionDTO.getCode() + " existe déjà");
        }

        Section section = new Section();
        section.setCode(sectionDTO.getCode());
        section.setNom(sectionDTO.getNom());
        section.setDescription(sectionDTO.getDescription());

        if (sectionDTO.getServiceId() != null) {
            ServiceEntite service = serviceRepository.findById(sectionDTO.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + sectionDTO.getServiceId()));
            section.setService(service);
        }

        if (sectionDTO.getChefSectionId() != null) {
            Collaborateur chef = collaborateurRepository.findById(sectionDTO.getChefSectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chef de section non trouvé avec l'id: " + sectionDTO.getChefSectionId()));
            section.setChefSection(chef);
        }

        Section savedSection = sectionRepository.save(section);
        return convertToDTO(savedSection);
    }

    // Mettre à jour une section
    @Transactional
    public SectionDTO updateSection(Long id, SectionDTO sectionDTO) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec l'id: " + id));

        section.setNom(sectionDTO.getNom());
        section.setDescription(sectionDTO.getDescription());

        if (sectionDTO.getServiceId() != null) {
            ServiceEntite service = serviceRepository.findById(sectionDTO.getServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + sectionDTO.getServiceId()));
            section.setService(service);
        }

        if (sectionDTO.getChefSectionId() != null) {
            Collaborateur chef = collaborateurRepository.findById(sectionDTO.getChefSectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chef de section non trouvé avec l'id: " + sectionDTO.getChefSectionId()));
            section.setChefSection(chef);
        } else {
            section.setChefSection(null);
        }

        Section updatedSection = sectionRepository.save(section);
        return convertToDTO(updatedSection);
    }

    // Supprimer une section
    @Transactional
    public void deleteSection(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec l'id: " + id));

        if (section.getCollaborateurs() != null && !section.getCollaborateurs().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer une section qui a des collaborateurs associés");
        }

        sectionRepository.delete(section);
    }

    // Assigner un chef de section
    @Transactional
    public SectionDTO assignerChefSection(Long sectionId, Long chefId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec l'id: " + sectionId));

        Collaborateur chef = collaborateurRepository.findById(chefId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef de section non trouvé avec l'id: " + chefId));

        section.setChefSection(chef);
        Section updatedSection = sectionRepository.save(section);
        return convertToDTO(updatedSection);
    }
}
