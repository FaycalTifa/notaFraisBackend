package com.example.notaFraisBackend.repository.poste;

import com.example.notaFraisBackend.entities.poste.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {

    Optional<Direction> findByCode(String code);

    Optional<Direction> findByNom(String nom);

    boolean existsByCode(String code);

    @Query("SELECT d FROM Direction d LEFT JOIN FETCH d.services WHERE d.id = :id")
    Optional<Direction> findByIdWithServices(@Param("id") Long id);

    @Query("SELECT d FROM Direction d LEFT JOIN FETCH d.collaborateurs WHERE d.id = :id")
    Optional<Direction> findByIdWithCollaborateurs(@Param("id") Long id);

    @Query("SELECT d FROM Direction d WHERE d.directeur.id = :directeurId")
    Optional<Direction> findByDirecteurId(@Param("directeurId") Long directeurId);

}
