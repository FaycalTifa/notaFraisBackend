package com.example.notaFraisBackend.repository;

import com.example.notaFraisBackend.dto.CollaborateurDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.entities.enume.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CollaborateurRepository extends JpaRepository<Collaborateur, Long> {

    // Authentification
    Optional<Collaborateur> findByEmail(String email);

    Optional<Collaborateur> findByMatricule(String matricule);

    boolean existsByEmail(String email);

    boolean existsByMatricule(String matricule);

    // Filtres par organisation
    List<Collaborateur> findByDirectionId(Long directionId);

    List<Collaborateur> findByServiceId(Long serviceId);

    List<Collaborateur> findBySectionId(Long sectionId);

    List<Collaborateur> findByRole(Role role);

    // Recherche par responsable
    List<Collaborateur> findByResponsableDirectId(Long responsableId);


    // Recherche textuelle
    @Query("SELECT c FROM Collaborateur c WHERE " +
            "LOWER(c.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.prenoms) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.matricule) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Collaborateur> rechercher(@Param("search") String search);

    // Collaborateurs qu'un utilisateur peut évaluer
    @Query("SELECT DISTINCT c FROM Collaborateur c " +
            "LEFT JOIN FETCH c.direction " +
            "LEFT JOIN FETCH c.service " +
            "LEFT JOIN FETCH c.section " +
            "WHERE (:role = 'ADMIN') OR " +
            "(:role = 'DIRECTEUR' AND c.direction.id = :directionId) OR " +
            "(:role = 'CHEF_SERVICE' AND c.service.id = :serviceId) OR " +
            "(:role = 'CHEF_SECTION' AND c.section.id = :sectionId)")
    List<Collaborateur> findEvaluables(
            @Param("role") String role,
            @Param("directionId") Long directionId,
            @Param("serviceId") Long serviceId,
            @Param("sectionId") Long sectionId);

    @Query("""
            SELECT new com.example.notaFraisBackend.dto.CollaborateurDTO(
                c.id,
                c.nom,
                c.prenoms,
                c.nomComplet,
                c.matricule,
                c.posteActuel,
                d.id,
                d.nom,
                s.id,
                s.nom,
                sec.id,
                sec.nom
            )
            FROM Collaborateur c
            LEFT JOIN c.direction d
            LEFT JOIN c.service s
            LEFT JOIN c.section sec
            """)
    List<CollaborateurDTO> findAllEvaluablesAdmin();


    @Query("""
            SELECT new com.example.notaFraisBackend.dto.CollaborateurDTO(
                c.id,
                c.nom,
                c.prenoms,
                c.nomComplet,
                c.matricule,
                c.posteActuel,
                d.id,
                d.nom,
                s.id,
                s.nom,
                sec.id,
                sec.nom
            )
            FROM Collaborateur c
            LEFT JOIN c.direction d
            LEFT JOIN c.service s
            LEFT JOIN c.section sec
            WHERE d.id = :directionId
            """)
    List<CollaborateurDTO> findEvaluablesByDirection(@Param("directionId") Long directionId);

    @Query("""
            SELECT new com.example.notaFraisBackend.dto.CollaborateurDTO(
                c.id,
                c.nom,
                c.prenoms,
                c.nomComplet,
                c.matricule,
                c.posteActuel,
                d.id,
                d.nom,
                s.id,
                s.nom,
                sec.id,
                sec.nom
            )
            FROM Collaborateur c
            LEFT JOIN c.direction d
            LEFT JOIN c.service s
            LEFT JOIN c.section sec
            WHERE s.id = :serviceId
            """)
    List<CollaborateurDTO> findEvaluablesByService(@Param("serviceId") Long serviceId);


    @Query("""
SELECT new com.example.notaFraisBackend.dto.CollaborateurDTO(
    c.id,
    c.nom,
    c.prenoms,
    c.nomComplet,
    c.matricule,
    c.posteActuel,
    d.id,
    d.nom,
    s.id,
    s.nom,
    sec.id,
    sec.nom
)
FROM Collaborateur c
LEFT JOIN c.direction d
LEFT JOIN c.service s
LEFT JOIN c.section sec
WHERE sec.id = :sectionId
""")
    List<CollaborateurDTO> findEvaluablesBySection(@Param("sectionId") Long sectionId);

    // Statistiques
    long countByDirectionId(Long directionId);

    long countByServiceId(Long serviceId);

    long countBySectionId(Long sectionId);

}
