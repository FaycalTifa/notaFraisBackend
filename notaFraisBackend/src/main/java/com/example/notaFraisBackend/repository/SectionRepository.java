package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByIsDeletedFalse();
    Optional<Section> findByCodeAndIsDeletedFalse(String code);

    @Query("SELECT s FROM Section s WHERE s.service.id = :serviceId AND s.isDeleted = false")
    List<Section> findByServiceId(@Param("serviceId") Long serviceId);

    // Ajoutez cette méthode pour charger les relations
    @Query("SELECT s FROM Section s LEFT JOIN FETCH s.service serv LEFT JOIN FETCH serv.direction WHERE s.isDeleted = false")
    List<Section> findAllWithServiceAndDirection();
}
