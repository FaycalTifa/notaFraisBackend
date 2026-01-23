package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByUsername(String email);
    Utilisateur findUtilisateurById(Long id);

  //  Utilisateur findByUsernameAndPassword(String username, String password);
  // 🔸 Requête insensible à la casse (username)
  @Query("SELECT u FROM Utilisateur u WHERE LOWER(u.username) = LOWER(:username) AND u.password = :password")
  Utilisateur findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    // Optionnel : pour vérifier si le username existe
    Utilisateur findByUsernameIgnoreCase(String username);
    List<Utilisateur> findAllByIsDeletedFalse();
}
