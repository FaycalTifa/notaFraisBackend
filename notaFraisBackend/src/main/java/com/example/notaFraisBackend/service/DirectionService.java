package com.example.notaFraisBackend.service;

import com.example.notaFraisBackend.entities.Direction;
import com.example.notaFraisBackend.repository.DirectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectionService {


    @Autowired
    private DirectionRepository directionRepository;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(DirectionService.class);


    public Direction saveDirection(Direction direction){
        logger.info("ajout direction en cours dans le service");

        direction = directionRepository.save(direction);
        logger.info("========== save direction  Service ===============" + direction);
        return direction;
    }

    public Direction save(Direction direction){
        logger.info("ajout direction en cours dans le service");
        direction = directionRepository.save(direction);
        logger.info("========== save direction  Service ===============" + direction);
        return direction;
    }

    public List<Direction> findAll(){
        List<Direction> directions = directionRepository.findAllByIsDeletedFalse();
        logger.info("========== List directions en cours dans le service traiter ===============\" + directions");
        return directions;
    }


    public Direction update(Long id, Direction direction) {

        Direction exitingDirection = directionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La  agence avec l'ID fourni n'existe pas."));
        // Mettez à jour les propriétés de la banque existante avec les détails de la nouvelle banque
        exitingDirection.setLibelle(direction.getLibelle()); // et ainsi de suite pour les autres propriétés...
        exitingDirection.setCode(direction.getCode()); // par exemple, si 'nom' est un champ de l'entité Banque

        // Enregistrez la periodiciteRemboursement mise à jour dans la base de données
        return directionRepository.save(exitingDirection);
    }

    public Direction delete(Long id, Direction direction) {

        Direction exixtingDirection = directionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La agence avec l'ID fourni n'existe pas."));
        // Mettez à jour les propriétés de agence existante avec les détails de la nouvelle agence

        exixtingDirection.setLibelle(direction.getLibelle()); // par exemple, si 'nom' est un champ de l'entité Banque
        exixtingDirection.setCode(direction.getCode()); // et ainsi de suite pour les autres propriétés...
        exixtingDirection.setDeleted(true); // et ainsi de suite pour les autres propriétés...
        // Enregistrez la periodiciteRemboursement mise à jour dans la base de données
        return directionRepository.save(exixtingDirection);
    }


}
