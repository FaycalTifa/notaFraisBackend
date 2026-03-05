package com.example.notaFraisBackend.resource;


import com.example.notaFraisBackend.dto.AuthDTO;
import com.example.notaFraisBackend.dto.LoginRequestDTO;
import com.example.notaFraisBackend.dto.LoginResponseDTO;
import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.security.JwtTokenProvider;
import com.example.notaFraisBackend.service.AuthService;
import com.example.notaFraisBackend.service.CollaborateurService;
import com.example.notaFraisBackend.service.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            User userDetails = (User) authentication.getPrincipal();
            Collaborateur collaborateur = collaborateurRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Collaborateur non trouvé"));

            // ✅ Créer une réponse complète avec toutes les infos
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("id", collaborateur.getId());
            response.put("email", collaborateur.getEmail());
            response.put("nom", collaborateur.getNom());
            response.put("prenoms", collaborateur.getPrenoms());
            response.put("nomComplet", collaborateur.getNomComplet());
            response.put("role", collaborateur.getRole().toString());

            // ✅ AJOUTER LES INFOS DE DIRECTION
            if (collaborateur.getDirection() != null) {
                response.put("directionId", collaborateur.getDirection().getId());
                response.put("directionNom", collaborateur.getDirection().getNom());
            }

            if (collaborateur.getService() != null) {
                response.put("serviceId", collaborateur.getService().getId());
                response.put("serviceNom", collaborateur.getService().getNom());
            }

            if (collaborateur.getSection() != null) {
                response.put("sectionId", collaborateur.getSection().getId());
                response.put("sectionNom", collaborateur.getSection().getNom());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }
    }

}
