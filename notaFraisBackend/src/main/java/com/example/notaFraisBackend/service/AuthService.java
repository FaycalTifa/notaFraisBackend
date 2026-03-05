package com.example.notaFraisBackend.service;


import com.example.notaFraisBackend.entities.entity.Collaborateur;
import com.example.notaFraisBackend.repository.CollaborateurRepository;
import com.example.notaFraisBackend.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CollaborateurRepository collaborateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public String authenticate(String email, String password) {
        System.out.println("AuthService.authenticate - Email: " + email);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        System.out.println("Token généré avec succès");
        return token;
    }

    public Collaborateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();
        return collaborateurRepository.findByEmail(email)
                .orElse(null);
    }

    // ✅ NOUVELLE MÉTHODE À AJOUTER ICI


    public void changePassword(String currentPassword, String newPassword) {
        Collaborateur currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        collaborateurRepository.save(currentUser);
    }

    // Dans AuthService.java
    public Map<String, Object> getCurrentUserInfo() {
        Collaborateur user = getCurrentUser();
        if (user == null) {
            return null;
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("nom", user.getNom());
        userInfo.put("prenoms", user.getPrenoms());
        userInfo.put("nomComplet", user.getNomComplet());
        userInfo.put("email", user.getEmail());
        userInfo.put("role", user.getRole());
        userInfo.put("actif", user.isActif());
        userInfo.put("matricule", user.getMatricule());
        userInfo.put("posteActuel", user.getPosteActuel());

        // AJOUTER LES INFORMATIONS DE DIRECTION
        if (user.getDirection() != null) {
            userInfo.put("directionId", user.getDirection().getId());
            userInfo.put("directionNom", user.getDirection().getNom());
        }

        // AJOUTER LES INFORMATIONS DE SERVICE
        if (user.getService() != null) {
            userInfo.put("serviceId", user.getService().getId());
            userInfo.put("serviceNom", user.getService().getNom());
        }

        // AJOUTER LES INFORMATIONS DE SECTION
        if (user.getSection() != null) {
            userInfo.put("sectionId", user.getSection().getId());
            userInfo.put("sectionNom", user.getSection().getNom());
        }

        return userInfo;
    }

    public List<Map<String, Object>> getAllUsers() {
        return collaborateurRepository.findAll().stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getId());
                    map.put("email", user.getEmail());
                    map.put("nom", user.getNom());
                    map.put("prenoms", user.getPrenoms());
                    map.put("role", user.getRole().toString());
                    map.put("service", user.getService());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
