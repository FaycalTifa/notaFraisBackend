package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    List<Agent> findAllByIsDeletedFalse();
    Optional<Agent> findByCodeAndIsDeletedFalse(String code);

    @Query("SELECT a FROM Agent a WHERE a.section.id = :sectionId AND a.isDeleted = false")
    List<Agent> findBySectionId(@Param("sectionId") Long sectionId);

    // Ajoutez cette méthode
    @Query("SELECT a FROM Agent a LEFT JOIN FETCH a.section s LEFT JOIN FETCH s.service serv LEFT JOIN FETCH serv.direction WHERE a.isDeleted = false")
    List<Agent> findAllWithRelations();
}
