package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findAllByIsDeletedFalse();

    List<Evaluation> findAll();
}
