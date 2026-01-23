package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.entities.Evaluation;
import com.example.notaFraisBackend.repository.DirectionRepository;
import com.example.notaFraisBackend.repository.EvaluationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(EvaluationService.class);


    public Evaluation saveDirection(Evaluation evaluation){
        logger.info("ajout evaluation en cours dans le service");

        evaluation = evaluationRepository.save(evaluation);
        logger.info("========== save evaluation  Service ===============" + evaluation);
        return evaluation;
    }

    public Evaluation save(Evaluation evaluation){
        logger.info("ajout direction en cours dans le service");
        evaluation = evaluationRepository.save(evaluation);
        logger.info("========== save evaluation  Service ===============" + evaluation);
        return evaluation;
    }

    public List<Evaluation> findAll(){
        List<Evaluation> evaluation = evaluationRepository.findAllByIsDeletedFalse();
        logger.info("========== List evaluation en cours dans le service traiter ===============\" + evaluation");
        return evaluation;
    }


    public Evaluation update(Long id, Evaluation evaluation) {

        Evaluation exitingEvaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La  agence avec l'ID fourni n'existe pas."));
        // Mettez à jour les propriétés de la banque existante avec les détails de la nouvelle banque
        exitingEvaluation.setNiveau(evaluation.getNiveau()); // et ainsi de suite pour les autres propriétés...

        // Enregistrez la periodiciteRemboursement mise à jour dans la base de données
        return evaluationRepository.save(exitingEvaluation);
    }

    public Evaluation delete(Long id, Evaluation evaluation) {

        Evaluation exitingEvaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La evaluation avec l'ID fourni n'existe pas."));
        // Mettez à jour les propriétés de agence existante avec les détails de la nouvelle agence

        exitingEvaluation.setNiveau(evaluation.getNiveau()); // par exemple, si 'nom' est un champ de l'entité Banque
        exitingEvaluation.setDeleted(true); // et ainsi de suite pour les autres propriétés...
        return evaluationRepository.save(exitingEvaluation);
    }

}
