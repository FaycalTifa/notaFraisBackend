package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {

    List<Direction> findAllByIsDeletedFalse();

}
