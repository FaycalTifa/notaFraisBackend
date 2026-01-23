package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.entities.Section;
import com.example.notaFraisBackend.entities.ServiceEntite;
import com.example.notaFraisBackend.repository.SectionRepository;
import com.example.notaFraisBackend.repository.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;
 @Autowired
    private ServiceRepository serviceRepository;

    private static final Logger logger = LoggerFactory.getLogger(SectionService.class);

    public Section saveT(Section section) {
        logger.info("Sauvegarde de la section: {}", section);
        return sectionRepository.save(section);
    }

    public Section save(Section section) {
        logger.info("Sauvegarde de la section: {}", section);

        // Si serviceId est fourni, charger l'objet Service complet
        if (section.getServiceId() != null) {
            ServiceEntite serviceEntite = serviceRepository.findById(section.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service non trouvé avec l'ID: " + section.getServiceId()));
            section.setService(serviceEntite);
        }

        Section savedSection = sectionRepository.save(section);

        // Détacher les relations pour éviter les références circulaires
        savedSection.setService(null);
        savedSection.setAgents(null);

        return savedSection;
    }

    public Section update(Long id, Section sectionDetails) {
        Section existingSection = sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section non trouvée avec l'ID: " + id));

        existingSection.setCode(sectionDetails.getCode());
        existingSection.setLibelle(sectionDetails.getLibelle());

        // Mettre à jour le service si serviceId est fourni
        if (sectionDetails.getServiceId() != null) {
            ServiceEntite serviceEntite = serviceRepository.findById(sectionDetails.getServiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Service non trouvé"));
            existingSection.setService(serviceEntite);
        } else {
            existingSection.setService(null);
        }

        return sectionRepository.save(existingSection);
    }



    public List<Section> findAll() {
        return sectionRepository.findAllWithServiceAndDirection();
    }

    public List<Section> findByServiceId(Long serviceId) {
        return sectionRepository.findByServiceId(serviceId);
    }

    public Section findById(Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Section non trouvée avec l'ID: " + id));
    }

    public Section updateT(Long id, Section sectionDetails) {
        Section existingSection = findById(id);

        existingSection.setCode(sectionDetails.getCode());
        existingSection.setLibelle(sectionDetails.getLibelle());
        existingSection.setService(sectionDetails.getService());

        return sectionRepository.save(existingSection);
    }

    public Section delete(Long id) {
        Section existingSection = findById(id);
        existingSection.setDeleted(true);
        return sectionRepository.save(existingSection);
    }
}
