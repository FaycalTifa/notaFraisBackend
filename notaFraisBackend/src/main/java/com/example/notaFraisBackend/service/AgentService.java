package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.entities.Agent;
import com.example.notaFraisBackend.entities.Section;
import com.example.notaFraisBackend.repository.AgentRepository;
import com.example.notaFraisBackend.repository.SectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;
 @Autowired
    private SectionRepository sectionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    public Agent save(Agent agent) {
        logger.info("Sauvegarde de l'agent: {}", agent);

        // CORRECTION : Charger la Section à partir de sectionId
        if (agent.getSectionId() != null) {
            Section section = sectionRepository.findById(agent.getSectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Section non trouvée avec l'ID: " + agent.getSectionId()));
            agent.setSection(section);
            logger.info("Section chargée: {}", section.getLibelle());
        } else {
            logger.warn("Aucun sectionId fourni pour l'agent");
        }

        Agent savedAgent = agentRepository.save(agent);
        logger.info("Agent sauvegardé avec ID: {}", savedAgent.getId());

        return savedAgent;
    }


    public List<Agent> findAll() {
        return agentRepository.findAllWithRelations(); // ← Utilisez la nouvelle méthode
    }

    public List<Agent> findBySectionId(Long sectionId) {
        return agentRepository.findBySectionId(sectionId);
    }

    public Agent findById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé avec l'ID: " + id));
    }

    public Agent update(Long id, Agent agentDetails) {
        Agent existingAgent = findById(id);
        existingAgent.setCode(agentDetails.getCode());
        existingAgent.setLibelle(agentDetails.getLibelle());

        // Mettre à jour la section si sectionId est fourni
        if (agentDetails.getId() != null) {
            // Vous devrez injecter SectionRepository pour charger la section complète
            // Section section = sectionRepository.findById(agentDetails.getSectionId())...
            // existingAgent.setSection(section);
        }

        return agentRepository.save(existingAgent);
    }

    public Agent delete(Long id) {
        Agent existingAgent = findById(id);
        existingAgent.setDeleted(true);
        return agentRepository.save(existingAgent);
    }
}
