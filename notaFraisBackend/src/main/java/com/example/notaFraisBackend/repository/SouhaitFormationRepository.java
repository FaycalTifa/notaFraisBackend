package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.entity.SouhaitFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SouhaitFormationRepository extends JpaRepository<SouhaitFormation, Long> {
    List<SouhaitFormation> findByEvaluationId(Long evaluationId);

    @Modifying
    @Query("DELETE FROM SouhaitFormation s WHERE s.evaluation.id = :evaluationId")
    void deleteByEvaluationId(@Param("evaluationId") Long evaluationId);
}
