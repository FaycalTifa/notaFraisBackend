package com.example.notaFraisBackend.resource;

import com.example.notaFraisBackend.dto.RegisterDTO;
import com.example.notaFraisBackend.entities.Utilisateur;
import com.example.notaFraisBackend.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Autorise Angular
@RequestMapping("/api/utilisateurs")
public class UtilisateurResource {

    @Autowired
    private UtilisateurService utilisateurService;
    private static final Logger logger = LoggerFactory.getLogger(UtilisateurResource.class);


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        System.out.println("==================login==================");
        System.out.println(username);
        System.out.println(password);
        System.out.println("===================login=================");

        Utilisateur utilisateur = utilisateurService.login(username, password);

        if (utilisateur != null) {
            System.out.println("===================if=================");
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login réussi ✅");
            response.put("user", utilisateur);
            return ResponseEntity.ok(response);
        } else {
            System.out.println("===================else=================");
            Map<String, String> response = new HashMap<>();
            response.put("message", "Identifiants invalides ❌");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        if(utilisateur != null) {
           utilisateur = utilisateurService.createUtilisateur(utilisateur);
           // return ResponseEntity.ok("Utilisateur créé ✅");
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
        } else {
            logger.warn("Requête invalide : banque est null [Code: {}]", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();        }
    }

    /**
     *
     * @param id
     * @param utilisateur
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        Utilisateur updatedUtilisateur = utilisateurService.update(id, utilisateur);
        return new ResponseEntity<>(updatedUtilisateur, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @param utilisateur
     * @return
     */
    @PutMapping("/deleteAgence/{id}")
    public ResponseEntity<Utilisateur> delete(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        Utilisateur deletedUtilisateur = utilisateurService.delete(id, utilisateur);
        return new ResponseEntity<>(deletedUtilisateur, HttpStatus.OK);
    }

    @PostMapping("/registerOK")
    public ResponseEntity<?> registerUtilisateurOK(@RequestBody RegisterDTO dto) {
        try {
            Utilisateur utilisateur = utilisateurService.createUtilisateur(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur serveur");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUtilisateur(@RequestBody RegisterDTO dto) {
        try {
            Utilisateur utilisateur = utilisateurService.createUtilisateur(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Utilisateur créé avec succès");
            response.put("utilisateur", utilisateur);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erreur de validation");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erreur serveur");
            error.put("message", "Une erreur interne est survenue");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Long id, @RequestBody RegisterDTO dto) {
        try {
            Utilisateur utilisateur = utilisateurService.updateUtilisateur(id, dto);
            return ResponseEntity.ok(new {
                message: "Utilisateur modifié avec succès",
                        utilisateur: utilisateur
            });
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new {
                error: "Erreur de validation",
                        message: e.getMessage()
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new {
                error: "Erreur serveur",
                        message: "Une erreur interne est survenue"
            });
        }
    }*/

}
