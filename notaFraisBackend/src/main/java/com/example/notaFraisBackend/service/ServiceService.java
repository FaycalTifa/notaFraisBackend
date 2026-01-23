package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.entities.ServiceEntite;
import com.example.notaFraisBackend.repository.DirectionRepository;
import com.example.notaFraisBackend.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceEntiteRepository;
    @Autowired
    private DirectionRepository directionRepository;

    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);

    public ServiceEntite saveT(ServiceEntite serviceEntite) {
        logger.info("Sauvegarde du service: {}", serviceEntite);
        return serviceEntiteRepository.save(serviceEntite);
    }
    public ServiceEntite save(ServiceEntite serviceEntite) {
        logger.info("Sauvegarde du service: {}", serviceEntite);

        // Si directionId est fourni, charger l'objet Direction complet
        if (serviceEntite.getDirectionId() != null) {
            Direction direction = directionRepository.findById(serviceEntite.getDirectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Direction non trouvée avec l'ID: " + serviceEntite.getDirectionId()));
            serviceEntite.setDirection(direction);
        }

        return serviceEntiteRepository.save(serviceEntite);
    }

    public ServiceEntite update(Long id, ServiceEntite serviceDetails) {
        ServiceEntite existingService = serviceEntiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service non trouvé avec l'ID: " + id));

        existingService.setCode(serviceDetails.getCode());
        existingService.setLibelle(serviceDetails.getLibelle());

        // Mettre à jour la direction si directionId est fourni
        if (serviceDetails.getDirectionId() != null) {
            Direction direction = directionRepository.findById(serviceDetails.getDirectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Direction non trouvée"));
            existingService.setDirection(direction);
        } else {
            existingService.setDirection(null);
        }

        return serviceEntiteRepository.save(existingService);
    }

    public List<ServiceEntite> findAll() {
        return serviceEntiteRepository.findAllByIsDeletedFalse();
    }

    public List<ServiceEntite> findByDirectionId(Long directionId) {
        return serviceEntiteRepository.findByDirectionId(directionId);
    }

    public ServiceEntite findById(Long id) {
        return serviceEntiteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service non trouvé avec l'ID: " + id));
    }

    public ServiceEntite updateT(Long id, ServiceEntite serviceDetails) {
        ServiceEntite existingService = findById(id);

        existingService.setCode(serviceDetails.getCode());
        existingService.setLibelle(serviceDetails.getLibelle());
     //   existingService.setDirection(serviceDetails.getDirection());

        return serviceEntiteRepository.save(existingService);
    }

    public ServiceEntite delete(Long id) {
        ServiceEntite existingService = findById(id);
        existingService.setDeleted(true);
        return serviceEntiteRepository.save(existingService);
    }
}
