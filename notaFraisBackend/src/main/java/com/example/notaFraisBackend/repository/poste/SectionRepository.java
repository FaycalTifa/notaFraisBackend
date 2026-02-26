package com.example.notaFraisBackend.repository.poste;

import com.example.notaFraisBackend.entities.poste.Section;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByCode(String code);
    Optional<Section> findByNom(String nom);
    boolean existsByCode(String code);

    @Query("SELECT s FROM Section s WHERE s.service.id = :serviceId")
    List<Section> findByServiceId(@Param("serviceId") Long serviceId);

    @Query("SELECT s FROM Section s WHERE s.chefSection.id = :chefId")
    Optional<Section> findByChefSectionId(@Param("chefId") Long chefId);
}
