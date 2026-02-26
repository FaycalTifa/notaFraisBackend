package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.dto.ancien.ServiceDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.repository.poste.DirectionRepository;
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
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    // Convertir Entity en DTO
    private ServiceDTO convertToDTO(ServiceEntite service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setCode(service.getCode());
        dto.setNom(service.getNom());
        dto.setDescription(service.getDescription());

        if (service.getDirection() != null) {
            dto.setDirectionId(service.getDirection().getId());
            dto.setDirectionNom(service.getDirection().getNom());
        }

        if (service.getChefService() != null) {
            dto.setChefServiceId(service.getChefService().getId());
            dto.setChefServiceNom(service.getChefService().getNomComplet());
        }

        dto.setNombreSections(service.getSections() != null ? service.getSections().size() : 0);
        dto.setNombreCollaborateurs(service.getCollaborateurs() != null ? service.getCollaborateurs().size() : 0);

        return dto;
    }

    // Récupérer tous les services
    @Transactional(readOnly = true)
    public List<ServiceDTO> getAllServices() {
        System.out.println("--------- LISTE SERVICE -------------");
        return serviceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un service par son ID
    @Transactional(readOnly = true)
    public ServiceDTO getServiceById(Long id) {
        ServiceEntite service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + id));
        return convertToDTO(service);
    }

    // Récupérer les services par direction
    @Transactional(readOnly = true)
    public List<ServiceDTO> getServicesByDirection(Long directionId) {
        return serviceRepository.findByDirectionId(directionId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Créer un nouveau service
    @Transactional
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        if (serviceRepository.existsByCode(serviceDTO.getCode())) {
            throw new RuntimeException("Un service avec le code " + serviceDTO.getCode() + " existe déjà");
        }

        ServiceEntite service = new ServiceEntite();
        service.setCode(serviceDTO.getCode());
        service.setNom(serviceDTO.getNom());
        service.setDescription(serviceDTO.getDescription());

        if (serviceDTO.getDirectionId() != null) {
            Direction direction = directionRepository.findById(serviceDTO.getDirectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec l'id: " + serviceDTO.getDirectionId()));
            service.setDirection(direction);
        }

        if (serviceDTO.getChefServiceId() != null) {
            Collaborateur chef = collaborateurRepository.findById(serviceDTO.getChefServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chef de service non trouvé avec l'id: " + serviceDTO.getChefServiceId()));
            service.setChefService(chef);
        }

        ServiceEntite savedService = serviceRepository.save(service);
        return convertToDTO(savedService);
    }

    // Mettre à jour un service
    @Transactional
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        ServiceEntite service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + id));

        service.setNom(serviceDTO.getNom());
        service.setDescription(serviceDTO.getDescription());

        if (serviceDTO.getDirectionId() != null) {
            Direction direction = directionRepository.findById(serviceDTO.getDirectionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Direction non trouvée avec l'id: " + serviceDTO.getDirectionId()));
            service.setDirection(direction);
        }

        if (serviceDTO.getChefServiceId() != null) {
            Collaborateur chef = collaborateurRepository.findById(serviceDTO.getChefServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Chef de service non trouvé avec l'id: " + serviceDTO.getChefServiceId()));
            service.setChefService(chef);
        } else {
            service.setChefService(null);
        }

        ServiceEntite updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }

    // Supprimer un service
    @Transactional
    public void deleteService(Long id) {
        ServiceEntite service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + id));

        if (service.getSections() != null && !service.getSections().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer un service qui a des sections associées");
        }

        serviceRepository.delete(service);
    }

    // Assigner un chef de service
    @Transactional
    public ServiceDTO assignerChefService(Long serviceId, Long chefId) {
        ServiceEntite service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service non trouvé avec l'id: " + serviceId));

        Collaborateur chef = collaborateurRepository.findById(chefId)
                .orElseThrow(() -> new ResourceNotFoundException("Chef de service non trouvé avec l'id: " + chefId));

        service.setChefService(chef);
        ServiceEntite updatedService = serviceRepository.save(service);
        return convertToDTO(updatedService);
    }
}
