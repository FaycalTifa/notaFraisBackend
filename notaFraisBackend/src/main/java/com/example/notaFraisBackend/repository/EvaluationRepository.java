package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.entity.Evaluation;
import com.example.notaFraisBackend.entities.enume.StatutEvaluation;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByCollaborateurId(Long collaborateurId);

    List<Evaluation> findByEvaluateurId(Long evaluateurId);

    List<Evaluation> findByAnnee(Integer annee);

    List<Evaluation> findByStatut(StatutEvaluation statut);

    @Query("SELECT e FROM Evaluation e WHERE e.collaborateur.direction.id = :directionId")
    List<Evaluation> findByDirectionId(@Param("directionId") Long directionId);

    @Query("SELECT e FROM Evaluation e WHERE e.collaborateur.service.id = :serviceId")
    List<Evaluation> findByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT e FROM Evaluation e WHERE e.collaborateur.section.id = :sectionId")
    List<Evaluation> findBySectionId(@Param("sectionId") Long sectionId);

    @Query("SELECT e FROM Evaluation e WHERE e.collaborateur.id = :collaborateurId AND e.annee = :annee")
    Optional<Evaluation> findByCollaborateurAndAnnee(@Param("collaborateurId") Long collaborateurId, @Param("annee") Integer annee);

    boolean existsByCollaborateurIdAndAnnee(Long collaborateurId, Integer annee);
}
