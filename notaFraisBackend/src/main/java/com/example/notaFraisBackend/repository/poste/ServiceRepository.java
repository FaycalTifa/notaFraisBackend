package com.example.notaFraisBackend.repository.poste;

import com.example.notaFraisBackend.entities.poste.Direction;
import com.example.notaFraisBackend.entities.poste.ServiceEntite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntite, Long> {

    Optional<ServiceEntite> findByCode(String code);
    Optional<ServiceEntite> findByNom(String nom);
    boolean existsByCode(String code);

    @Query("SELECT s FROM ServiceEntite s WHERE s.direction.id = :directionId")
    List<ServiceEntite> findByDirectionId(@Param("directionId") Long directionId);

    @Query("SELECT s FROM ServiceEntite s LEFT JOIN FETCH s.sections WHERE s.id = :id")
    Optional<ServiceEntite> findByIdWithSections(@Param("id") Long id);

    @Query("SELECT s FROM ServiceEntite s WHERE s.chefService.id = :chefId")
    Optional<ServiceEntite> findByChefServiceId(@Param("chefId") Long chefId);
}
