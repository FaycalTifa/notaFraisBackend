package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.entity.ObjectifFutur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectifFuturRepository extends JpaRepository<ObjectifFutur, Long> {
    List<ObjectifFutur> findByEvaluationId(Long evaluationId);

    @Modifying
    @Query("DELETE FROM ObjectifFutur o WHERE o.evaluation.id = :evaluationId")
    void deleteByEvaluationId(@Param("evaluationId") Long evaluationId);
}
